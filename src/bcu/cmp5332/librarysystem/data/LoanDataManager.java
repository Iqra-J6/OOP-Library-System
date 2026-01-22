package bcu.cmp5332.librarysystem.data;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.model.Loan;
import bcu.cmp5332.librarysystem.model.Patron;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Loads and stores loan data (patronId, bookId, startDate, dueDate).
 * Uses the Loan stored inside each Book.
 */
public class LoanDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/loans.txt";

    @Override
    public void loadData(Library library) throws IOException, LibraryException {
        File f = new File(RESOURCE);
        if (!f.exists()) return; // no loans file yet is fine

        try (Scanner sc = new Scanner(f)) {
            int lineIdx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) { lineIdx++; continue; }

                String[] p = line.split(SEPARATOR, -1);
                try {
                    int patronId = Integer.parseInt(p[0]);
                    int bookId = Integer.parseInt(p[1]);
                    LocalDate startDate = LocalDate.parse(p[2]);
                    LocalDate dueDate = LocalDate.parse(p[3]);

                    Patron patron = library.getPatronByID(patronId);
                    Book book = library.getBookByID(bookId);

                    // If old data contains deleted entities, skip to avoid inconsistent state
                    if (patron.isDeleted() || book.isDeleted()) {
                        lineIdx++;
                        continue;
                    }

                    Loan loan = new Loan(patron, book, startDate, dueDate);
                    book.setLoan(loan);

                    // Ensure patron's borrowed list matches loan state
                    patron.addBook(book);

                } catch (NumberFormatException ex) {
                    throw new LibraryException("Unable to parse loan data on line " + lineIdx + "\nError: " + ex);
                }
                lineIdx++;
            }
        }
    }

    @Override
    public void storeData(Library library) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Book book : library.getAllBooks()) {
                if (book.isDeleted() || !book.isOnLoan()) continue;

                Loan loan = book.getLoan();
                if (loan == null) continue;

                Patron patron = loan.getPatron();
                if (patron == null || patron.isDeleted()) continue;

                out.print(patron.getId() + SEPARATOR);
                out.print(book.getId() + SEPARATOR);
                out.print(loan.getStartDate() + SEPARATOR);
                out.print(loan.getDueDate());
                out.println();
            }
        }
    }
}

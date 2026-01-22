package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Library;

import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;

import static bcu.cmp5332.librarysystem.data.DataManager.SEPARATOR;

public class ShowLoanHistory implements Command {

    private final int patronId;
    private final String RESOURCE = "./resources/data/loan_history.txt";

    public ShowLoanHistory(int patronId) {
        this.patronId = patronId;
    }

    @Override
    public void execute(Library library, LocalDate currentDate) throws LibraryException {
        File f = new File(RESOURCE);
        if (!f.exists()) {
            System.out.println("No loan history found.");
            return;
        }

        int count = 0;

        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] p = line.split(SEPARATOR, -1);
                int pid = Integer.parseInt(p[0]);
                if (pid != patronId) continue;

                int bookId = Integer.parseInt(p[1]);
                String action = p[2];
                String date = p[3];
                String due = (p.length > 4 ? p[4] : "");

                String title = "(unknown title)";
                try {
                    Book b = library.getBookByID(bookId);
                    title = b.getTitle();
                } catch (LibraryException ignored) { }

                if (due != null && !due.isBlank()) {
                    System.out.println(action + " | " + date + " | Book #" + bookId + " - " + title + " | Due: " + due);
                } else {
                    System.out.println(action + " | " + date + " | Book #" + bookId + " - " + title);
                }

                count++;
            }
        } catch (Exception ex) {
            throw new LibraryException("Could not read loan history file: " + ex.getMessage());
        }

        System.out.println(count + " record(s).");
    }
}

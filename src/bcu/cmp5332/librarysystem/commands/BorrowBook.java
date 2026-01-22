package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.model.Loan;
import bcu.cmp5332.librarysystem.model.Patron;

import java.time.LocalDate;

public class BorrowBook implements Command {

    private final int patronId;
    private final int bookId;

    public BorrowBook(int patronId, int bookId) {
        this.patronId = patronId;
        this.bookId = bookId;
    }

    @Override
    public void execute(Library library, LocalDate currentDate) throws LibraryException {
        Book book = library.getBookByID(bookId);
        Patron patron = library.getPatronByID(patronId);

        // ✅ 7.3 enforce max 2 books (uses your Library rule)
        if (patron.getBorrowedBookCount() >= library.getMaxLoansPerPatron()) {
            throw new LibraryException("Borrowing limit reached. A patron may borrow up to "
                    + library.getMaxLoansPerPatron() + " book(s).");
        }

        if (book.isDeleted()) {
            throw new LibraryException("Cannot borrow a deleted book.");
        }
        if (patron.isDeleted()) {
            throw new LibraryException("Cannot borrow with a deleted patron.");
        }
        if (book.isOnLoan()) {
            throw new LibraryException("Book is already on loan.");
        }

        LocalDate dueDate = currentDate.plusDays(library.getLoanPeriod());
        Loan loan = new Loan(patron, book, currentDate, dueDate);

        book.setLoan(loan);
        patron.borrowBook(book, dueDate);

        System.out.println("Book borrowed successfully. Due date: " + dueDate);
        try {
            bcu.cmp5332.librarysystem.data.LoanHistoryLogger
                    .logBorrow(patronId, bookId, currentDate, dueDate);
        } catch (java.io.IOException ignored) { }
    }
}

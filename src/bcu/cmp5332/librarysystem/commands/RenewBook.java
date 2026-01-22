package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.model.Loan;
import bcu.cmp5332.librarysystem.model.Patron;

import java.time.LocalDate;

public class RenewBook implements Command {

    private final int patronId;
    private final int bookId;

    public RenewBook(int patronId, int bookId) {
        this.patronId = patronId;
        this.bookId = bookId;
    }

    @Override
    public void execute(Library library, LocalDate currentDate) throws LibraryException {
        Book book = library.getBookByID(bookId);
        Patron patron = library.getPatronByID(patronId);

        if (!book.isOnLoan()) {
            throw new LibraryException("Book is not currently on loan.");
        }

        Loan loan = book.getLoan();
        if (loan.getPatron().getId() != patronId) {
            throw new LibraryException("This book is not borrowed by this patron.");
        }

        LocalDate newDueDate = currentDate.plusDays(library.getLoanPeriod());
        loan.renewDueDate(newDueDate);
        patron.renewBook(book, newDueDate);

        System.out.println("Book renewed successfully. New due date: " + newDueDate);
        try {
            bcu.cmp5332.librarysystem.data.LoanHistoryLogger
                    .logRenew(patronId, bookId, currentDate, newDueDate);
        } catch (java.io.IOException ignored) { }
    }
}

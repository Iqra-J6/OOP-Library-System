package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.model.Patron;

import java.time.LocalDate;

public class ReturnBook implements Command {

    private final int patronId;
    private final int bookId;

    public ReturnBook(int patronId, int bookId) {
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

        if (book.getLoan().getPatron().getId() != patronId) {
            throw new LibraryException("This book is not borrowed by this patron.");
        }

        book.returnToLibrary();
        patron.returnBook(book);

        System.out.println("Book returned successfully.");
        
        try {
            bcu.cmp5332.librarysystem.data.LoanHistoryLogger
                    .logReturn(patronId, bookId, currentDate);
        } catch (java.io.IOException ignored) { }

    }
}

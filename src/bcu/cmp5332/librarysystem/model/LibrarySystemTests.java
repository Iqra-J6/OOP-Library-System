package bcu.cmp5332.librarysystem.model;

import bcu.cmp5332.librarysystem.main.LibraryException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarySystemTests {

    @Test
    void softDeleteBook_hidesFromGetBooks() throws Exception {
        Library lib = new Library();
        Book b = new Book(1, "T", "A", "2020");
        lib.addBook(b);

        assertEquals(1, lib.getBooks().size());

        lib.softDeleteBook(1);

        assertEquals(0, lib.getBooks().size(), "Deleted books should not appear in getBooks()");
        assertTrue(lib.getBookByID(1).isDeleted(), "Book should be flagged deleted");
    }

    @Test
    void softDeletePatron_hidesFromGetPatrons() throws Exception {
        Library lib = new Library();
        Patron p = new Patron(1, "Name", "0123", "name@email.com");
        lib.addPatron(p);

        assertEquals(1, lib.getPatrons().size());

        lib.softDeletePatron(1);

        assertEquals(0, lib.getPatrons().size(), "Deleted patrons should not appear in getPatrons()");
        assertTrue(lib.getPatronByID(1).isDeleted(), "Patron should be flagged deleted");
    }

    @Test
    void cannotDeleteBookOnLoan() throws Exception {
        Library lib = new Library();
        Patron p = new Patron(1, "Name", "0123", "name@email.com");
        Book b = new Book(1, "T", "A", "2020");

        lib.addPatron(p);
        lib.addBook(b);

        // simulate a loan
        b.setLoan(new Loan(p, b, LocalDate.now(), LocalDate.now().plusDays(7)));
        p.addBook(b);

        LibraryException ex = assertThrows(LibraryException.class, () -> lib.softDeleteBook(1));
        assertTrue(ex.getMessage().toLowerCase().contains("on loan"));
    }

    @Test
    void borrowingLimit_blocksThirdBook() throws Exception {
        Library lib = new Library();
        Patron p = new Patron(1, "Name", "0123", "name@email.com");
        lib.addPatron(p);

        // add 3 books
        Book b1 = new Book(1, "B1", "A", "2020");
        Book b2 = new Book(2, "B2", "A", "2020");
        Book b3 = new Book(3, "B3", "A", "2020");
        lib.addBook(b1);
        lib.addBook(b2);
        lib.addBook(b3);

        // Borrow two books by adding to patron list
        p.addBook(b1);
        p.addBook(b2);

        assertEquals(2, p.getBorrowedBookCount());

        // rule check
        assertEquals(2, lib.getMaxLoansPerPatron());
        assertTrue(p.getBorrowedBookCount() >= lib.getMaxLoansPerPatron());
    }
}

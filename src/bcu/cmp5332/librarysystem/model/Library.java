package bcu.cmp5332.librarysystem.model;

import bcu.cmp5332.librarysystem.main.LibraryException;
import java.util.*;

/**
 * Represents the core library system.
 * 
 * The Library class manages all books and patrons, enforces borrowing rules,
 * and provides operations such as adding entities and soft-deleting books
 * and patrons. Deleted entities are retained in the system but hidden
 * from listings.
 */

public class Library {
    
	//Default loan period in days
    private final int loanPeriod = 7;
    //Stores all patrons indexed by ID
    private final Map<Integer, Patron> patrons = new TreeMap<>();
    //Stores all books indexed by ID
    private final Map<Integer, Book> books = new TreeMap<>();
    //Maximum number of books a patron can borrow at one time
    private final int maxLoansPerPatron = 2;


    public int getLoanPeriod() {
        return loanPeriod;
    }
    
    /**
     * Returns the maximum number of books a patron is allowed to borrow.
     *
     * @return the maximum loan limit per patron
     */
    public int getMaxLoansPerPatron() {
        return maxLoansPerPatron;
    }

    /**
     * Returns a list of all non-deleted books in the library.
     *
     * @return an unmodifiable list of active books
     */
    public List<Book> getBooks() {
        List<Book> out = new ArrayList<>();
        for (Book b : books.values()) {
            if (!b.isDeleted()) {
                out.add(b);
            }
        }
        return Collections.unmodifiableList(out);
    }

    public Book getBookByID(int id) throws LibraryException {
        if (!books.containsKey(id)) {
            throw new LibraryException("There is no such book with that ID.");
        }
        return books.get(id);
    }

    public Patron getPatronByID(int id) throws LibraryException {
        if (!patrons.containsKey(id)) {
            throw new LibraryException("There is no such patron with that ID.");
        }
        return patrons.get(id);
    }

    public void addBook(Book book) {
        if (books.containsKey(book.getId())) {
            throw new IllegalArgumentException("Duplicate book ID.");
        }
        books.put(book.getId(), book);
    }

    public void addPatron(Patron patron) {
        if (patrons.containsKey(patron.getId())) {
            throw new IllegalArgumentException("Duplicate patron ID.");
        }
        patrons.put(patron.getId(), patron);
    }
    
    /**
     * Returns a list of all non-deleted patrons in the library.
     *
     * @return an unmodifiable list of active patrons
     */
    public List<Patron> getPatrons() {
        List<Patron> out = new ArrayList<>();
        for (Patron p : patrons.values()) {
            if (!p.isDeleted()) {
                out.add(p);
            }
        }
        return Collections.unmodifiableList(out);
    }
    
    /**
     * Soft deletes a book from the library.
     * The book is not removed from storage but is marked as deleted
     * and hidden from listings.
     *
     * A book cannot be deleted if it is currently on loan.
     *
     * @param bookId the ID of the book to delete
     * @throws LibraryException if the book does not exist, is already deleted,
     *                          or is currently on loan
     */
    public void softDeleteBook(int bookId) throws LibraryException {
        Book book = getBookByID(bookId);

        if (book.isDeleted()) {
            throw new LibraryException("Book is already deleted.");
        }
        if (book.isOnLoan()) {
            throw new LibraryException("Cannot delete a book that is currently on loan.");
        }

        book.markDeleted();
    }
    
    /**
     * Soft deletes a patron from the library.
     * The patron is not removed from storage but is marked as deleted
     * and hidden from listings.
     *
     * A patron cannot be deleted if they currently have borrowed books.
     *
     * @param patronId the ID of the patron to delete
     * @throws LibraryException if the patron does not exist, is already deleted,
     *                          or has books on loan
     */
    public void softDeletePatron(int patronId) throws LibraryException {
        Patron patron = getPatronByID(patronId);

        if (patron.isDeleted()) {
            throw new LibraryException("Patron is already deleted.");
        }
        if (patron.hasBorrowedBooks()) {
            throw new LibraryException("Cannot delete a patron who currently has books on loan.");
        }

        patron.markDeleted();
    }
    
    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(new ArrayList<>(books.values()));
    }

    public List<Patron> getAllPatrons() {
        return Collections.unmodifiableList(new ArrayList<>(patrons.values()));
    }


}
 
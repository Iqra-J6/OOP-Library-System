package bcu.cmp5332.librarysystem.model;

import bcu.cmp5332.librarysystem.main.LibraryException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library patron.
 *
 * A patron can borrow, renew, and return books. Patrons are
 * soft-deleted when removed, meaning they remain in the system
 * but are hidden from listings. A patron cannot be deleted if
 * they currently have borrowed books.
 */
public class Patron {
    
	// Unique identifier for the patron
    private int id;
    // Patron's full name
    private String name;
    // Patron's contact phone number
    private String phone;
    // List of books currently borrowed by the patron
    private String email;
    private final List<Book> books = new ArrayList<>();
    // Indicates whether the patron has been soft-deleted
    private boolean deleted = false;
    
    public Patron(int id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    
    public void borrowBook(Book book, LocalDate dueDate) throws LibraryException {
        if (deleted) {
            throw new LibraryException("Deleted patron cannot borrow books.");
        }
        if (book.isDeleted()) {
            throw new LibraryException("Deleted book cannot be borrowed.");
        }
        if (books.contains(book)) {
            throw new LibraryException("Patron already has this book.");
        }
        if (book.isOnLoan() && book.getLoan().getPatron().getId() != this.id) {
            throw new LibraryException("Book is already on loan to someone else.");
        }
        books.add(book);
    }

    public void renewBook(Book book, LocalDate dueDate) throws LibraryException {
        if (!books.contains(book)) {
            throw new LibraryException("Patron does not have this book.");
        }
        // loan due date is updated in RenewBook via Loan. This keeps Patron consistent.
    }

    public void returnBook(Book book) throws LibraryException {
        if (!books.contains(book)) {
            throw new LibraryException("Patron does not have this book.");
        }
        books.remove(book);
    }
    
    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
        }
    }
    
    /**
     * Checks whether the patron has been soft-deleted.
     *
     * @return true if the patron is marked as deleted, false otherwise
     */
    public boolean isDeleted() {
    		return deleted;
    }
    
    /**
     * Marks the patron as deleted without removing them from storage.
     */
    public void markDeleted() {
    		this.deleted = true;
    }
    
    /**
     * Checks whether the patron currently has any borrowed books.
     *
     * @return true if the patron has borrowed books, false otherwise
     */
    public boolean hasBorrowedBooks() {
    		return !books.isEmpty();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
    
    /**
     * Returns the number of books currently borrowed by the patron.
     *
     * @return current borrowed book count
     */
    public int getBorrowedBookCount() {
        return books.size();
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<Book> getBooks() {
        return new ArrayList<>(books); // returns a copy for safety
    }

    
}
 
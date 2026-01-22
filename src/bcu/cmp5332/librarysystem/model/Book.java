package bcu.cmp5332.librarysystem.model;

import bcu.cmp5332.librarysystem.main.LibraryException;
import java.time.LocalDate;

/**
 * Represents a book in the library system.
 *
 * A book contains bibliographic information and may be associated
 * with a loan when borrowed. Books are soft-deleted, meaning they
 * are marked as deleted and hidden from listings rather than being
 * permanently removed from the system.
 */
public class Book {

    // Unique identifier for the book
    private int id;

    // Book title
    private String title;

    // Book author
    private String author;

    // Year the book was published
    private String publicationYear;

    // Publisher of the book
    private String publisher;

    // Current loan associated with this book (null if not on loan)
    private Loan loan;

    // Indicates whether the book has been soft-deleted
    private boolean deleted = false;

    /**
     * Creates a new Book without a publisher (backwards compatible).
     */
    public Book(int id, String title, String author, String publicationYear) {
        this(id, title, author, publicationYear, "");
    }

    /**
     * Creates a new Book with a publisher.
     */
    public Book(int id, String title, String author, String publicationYear, String publisher) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
    }

    public int getId() {
        return id;
    } 

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void returnToLibrary() {
        loan = null;
    }

    public boolean isOnLoan() {
        return loan != null;
    }

    public String getDetailsShort() {
        return "Book #" + id + " - " + title;
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append("Book #").append(id).append("\n");
        sb.append("Title: ").append(title).append("\n");
        sb.append("Author: ").append(author).append("\n");
        sb.append("Publication Year: ").append(publicationYear).append("\n");
        if (!publisher.isBlank()) {
            sb.append("Publisher: ").append(publisher).append("\n");
        }
        sb.append("Status: ").append(getStatus()).append("\n");
        return sb.toString();
    }

    public String getStatus() {
        if (isOnLoan()) {
            Patron p = loan.getPatron();
            return "On loan to Patron #" + p.getId() + " (due " + loan.getDueDate() + ")";
        }
        return "Available";
    }

    public LocalDate getDueDate() {
        if (!isOnLoan()) {
            return null;
        }
        return loan.getDueDate();
    }

    public void setDueDate(LocalDate dueDate) throws LibraryException {
        if (!isOnLoan()) {
            throw new LibraryException("Cannot set due date: book is not currently on loan.");
        }
        if (dueDate == null) {
            throw new LibraryException("Due date cannot be null.");
        }
        loan.renewDueDate(dueDate);
    }

    /**
     * Checks whether the book has been soft-deleted.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Marks the book as deleted without removing it from storage.
     */
    public void markDeleted() {
        this.deleted = true;
    }
}

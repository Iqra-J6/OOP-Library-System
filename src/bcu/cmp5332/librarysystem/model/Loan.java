package bcu.cmp5332.librarysystem.model;

import java.time.LocalDate;

/**
 * Represents a loan of a book to a patron.
 */
public class Loan {

    private final Patron patron;
    private final Book book;
    private final LocalDate startDate;
    private LocalDate dueDate;

    public Loan(Patron patron, Book book, LocalDate startDate, LocalDate dueDate) {
        this.patron = patron;
        this.book = book;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public void renewDueDate(LocalDate newDueDate) {
        this.dueDate = newDueDate;
    }

    public Patron getPatron() {
        return patron;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}

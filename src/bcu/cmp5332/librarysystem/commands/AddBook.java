package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.main.LibraryException;

import java.time.LocalDate;

public class AddBook implements Command {

    private final String title;
    private final String author;
    private final String publicationYear;
    private final String publisher;

    // Backwards compatible constructor (if any old code still calls it)
    public AddBook(String title, String author, String publicationYear) {
        this(title, author, publicationYear, "");
    }

    //  New constructor with publisher (required for checklist 5.1)
    public AddBook(String title, String author, String publicationYear, String publisher) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
    }

    @Override
    public void execute(Library library, LocalDate currentDate) throws LibraryException {
        // Safe next-ID generation (works even with soft deletes)
        int newId = 1;
        for (Book b : library.getAllBooks()) {
            if (b.getId() >= newId) {
                newId = b.getId() + 1;
            }
        }

        Book book = new Book(newId, title, author, publicationYear, publisher);
        library.addBook(book);

        System.out.println("Book #" + book.getId() + " added.");
    }
}

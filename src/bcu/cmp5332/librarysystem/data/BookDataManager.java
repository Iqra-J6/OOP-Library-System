package bcu.cmp5332.librarysystem.data;

import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.main.LibraryException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class BookDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/books.txt";

    @Override
    public void loadData(Library library) throws IOException, LibraryException {
        File f = new File(RESOURCE);
        if (!f.exists()) return; // no books file yet is fine

        try (Scanner sc = new Scanner(f)) {
            int line_idx = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isBlank()) {
                    line_idx++;
                    continue;
                }

                String[] properties = line.split(SEPARATOR, -1);

                try {
                    int id = Integer.parseInt(properties[0]);
                    String title = properties[1];
                    String author = properties[2];
                    String publicationYear = properties[3];

                    String publisher = "";
                    boolean deleted = false;

                    if (properties.length >= 6) {
                        // NEW format: id|title|author|year|publisher|deleted
                        publisher = properties[4];
                        if (!properties[5].isBlank()) {
                            deleted = Boolean.parseBoolean(properties[5]);
                        }
                    } else if (properties.length >= 5) {
                        // OLD format: id|title|author|year|deleted
                        if (!properties[4].isBlank()) {
                            deleted = Boolean.parseBoolean(properties[4]);
                        }
                    }

                    Book book = new Book(id, title, author, publicationYear, publisher);

                    if (deleted) {
                        book.markDeleted();
                    }

                    library.addBook(book);

                } catch (NumberFormatException ex) {
                    throw new LibraryException("Unable to parse book id " + properties[0]
                            + " on line " + line_idx + "\nError: " + ex);
                }

                line_idx++;
            }
        }
    }

    @Override
    public void storeData(Library library) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Book book : library.getAllBooks()) {
                out.print(book.getId() + SEPARATOR);
                out.print(book.getTitle() + SEPARATOR);
                out.print(book.getAuthor() + SEPARATOR);
                out.print(book.getPublicationYear() + SEPARATOR);
                out.print(book.getPublisher() + SEPARATOR);   // ✅ publisher saved
                out.print(book.isDeleted() + SEPARATOR);
                out.println();
            }
        }
    }
}

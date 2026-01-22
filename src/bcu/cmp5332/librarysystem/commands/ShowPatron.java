package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.model.Patron;

import java.time.LocalDate;

public class ShowPatron implements Command {

    private final int patronId;

    public ShowPatron(int patronId) {
        this.patronId = patronId;
    }

    @Override
    public void execute(Library library, LocalDate currentDate) throws LibraryException {
        Patron patron = library.getPatronByID(patronId);

        System.out.println("Patron #" + patron.getId());
        System.out.println("Name: " + patron.getName());
        System.out.println("Phone: " + patron.getPhone());
        System.out.println("Email: " + patron.getEmail());
        System.out.println("Books on loan: " + patron.getBorrowedBookCount());

        if (patron.getBorrowedBookCount() > 0) {
            System.out.println("Borrowed books:");
            for (Book b : patron.getBooks()) {
                System.out.println(" - " + b.getDetailsShort());
            }
        }
    }
}

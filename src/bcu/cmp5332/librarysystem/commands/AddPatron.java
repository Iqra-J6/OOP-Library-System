package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.model.Patron;

import java.time.LocalDate;

public class AddPatron implements Command {

    private final String name;
    private final String phone;
    private final String email;

    public AddPatron(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public void execute(Library library, LocalDate currentDate) throws LibraryException {

        //  Generate next ID safely (even if patrons were deleted)
        int newId = 1;
        for (Patron p : library.getAllPatrons()) {
            if (p.getId() >= newId) {
                newId = p.getId() + 1;
            }
        }

        Patron patron = new Patron(newId, name, phone, email);
        library.addPatron(patron);

        System.out.println("Patron added with ID: " + newId);
    }
}

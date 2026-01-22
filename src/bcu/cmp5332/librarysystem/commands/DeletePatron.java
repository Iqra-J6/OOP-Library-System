package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Library;

import java.time.LocalDate;

/**
 * Command to soft delete a patron by ID.
 */
public class DeletePatron implements Command {

    private final int patronId;

    public DeletePatron(int patronId) {
        this.patronId = patronId;
    }

    @Override
    public void execute(Library library, LocalDate currentDate) throws LibraryException {
        library.softDeletePatron(patronId);
        System.out.println("Patron #" + patronId + " deleted (soft delete).");
    }
}

package bcu.cmp5332.librarysystem.commands;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.model.Patron;
import bcu.cmp5332.librarysystem.commands.ListPatrons;


import java.time.LocalDate;

/**
 * Command to list all non-deleted patrons in the library.
 */
public class ListPatrons implements Command {

    @Override
    public void execute(Library library, LocalDate currentDate) throws LibraryException {
        for (Patron p : library.getPatrons()) {
            // Print minimal info to avoid depending on unfinished Patron methods
            System.out.println("Patron #" + p.getId());
        }
        System.out.println(library.getPatrons().size() + " patron(s)");
    }
}

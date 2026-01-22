package bcu.cmp5332.librarysystem.data;

import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Library;
import bcu.cmp5332.librarysystem.model.Patron;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class PatronDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/patrons.txt";

    @Override
    public void loadData(Library library) throws IOException, LibraryException {
        File f = new File(RESOURCE);
        if (!f.exists()) return; // no patrons file yet is fine

        try (Scanner sc = new Scanner(f)) {
            int lineIdx = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isBlank()) {
                    lineIdx++;
                    continue;
                }

                String[] p = line.split(SEPARATOR, -1);

                try {
                    int id = Integer.parseInt(p[0]);
                    String name = p[1];
                    String phone = p[2];

                    // ✅ NEW: email (may not exist in old files)
                    String email = "";
                    if (p.length > 3) {
                        email = p[3];
                    }

                    // ✅ deleted flag (shifted to index 4)
                    boolean deleted = false;
                    if (p.length > 4 && !p[4].isBlank()) {
                        deleted = Boolean.parseBoolean(p[4]);
                    }

                    Patron patron = new Patron(id, name, phone, email);
                    if (deleted) {
                        patron.markDeleted();
                    }

                    library.addPatron(patron);

                } catch (NumberFormatException ex) {
                    throw new LibraryException(
                        "Unable to parse patron id " + p[0] +
                        " on line " + lineIdx + "\nError: " + ex
                    );
                }

                lineIdx++;
            }
        }
    }

    @Override
    public void storeData(Library library) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Patron patron : library.getAllPatrons()) {
                out.print(patron.getId() + SEPARATOR);
                out.print(patron.getName() + SEPARATOR);
                out.print(patron.getPhone() + SEPARATOR);
                out.print(patron.getEmail() + SEPARATOR);   // ✅ email stored
                out.print(patron.isDeleted() + SEPARATOR);
                out.println();
            }
        }
    }
}

package bcu.cmp5332.librarysystem.main;

import bcu.cmp5332.librarysystem.commands.LoadGUI;
import bcu.cmp5332.librarysystem.commands.ListBooks;
import bcu.cmp5332.librarysystem.commands.AddBook;
import bcu.cmp5332.librarysystem.commands.Command;
import bcu.cmp5332.librarysystem.commands.Help;
import bcu.cmp5332.librarysystem.commands.ListPatrons;
import bcu.cmp5332.librarysystem.commands.AddPatron;
import bcu.cmp5332.librarysystem.commands.DeleteBook;
import bcu.cmp5332.librarysystem.commands.DeletePatron;
import bcu.cmp5332.librarysystem.commands.BorrowBook;
import bcu.cmp5332.librarysystem.commands.RenewBook;
import bcu.cmp5332.librarysystem.commands.ReturnBook;
import bcu.cmp5332.librarysystem.commands.ShowBook;
import bcu.cmp5332.librarysystem.commands.ShowPatron;
import bcu.cmp5332.librarysystem.commands.ShowLoanHistory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CommandParser {
    
	public static Command parse(String line) throws IOException, LibraryException {
		

	    try {
	    	
	    	line = line.trim();
	    	String[] parts = line.split("\\s+");
	    	String cmd = parts[0];


	    	if (cmd.equals("addbook")) {
	    	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    	    System.out.print("Title: ");
	    	    String title = br.readLine();
	    	    System.out.print("Author: ");
	    	    String author = br.readLine();
	    	    System.out.print("Publication Year: ");
	    	    String publicationYear = br.readLine();
	    	    System.out.print("Publisher: ");
	    	    String publisher = br.readLine();

	    	    return new AddBook(title, author, publicationYear, publisher);
	    	}

             else if (cmd.equals("addpatron")) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Name: ");
                String name = br.readLine();
                System.out.print("Phone: ");
                String phone = br.readLine();
                System.out.print("Email: ");
                String email = br.readLine();

                return new AddPatron(name, phone, email);
            }
                
            else if (parts.length == 1) {
                if (cmd.equals("listbooks")) {
                    return new ListBooks();
                } else if (cmd.equals("listpatrons")) {
                    return new ListPatrons();
                } else if (cmd.equals("loadgui")) {
                    return new LoadGUI();
                } else if (cmd.equals("help")) {
                    return new Help();
                } else if (cmd.equals("exit")) {
                    // handled elsewhere (or just let it fall through)
                }
            }

            
             else if (parts.length == 2) {
                int id = Integer.parseInt(parts[1]);

                if (cmd.equals("deletebook")) {
                    return new DeleteBook(id);

                } else if (cmd.equals("deletepatron")) {
                    return new DeletePatron(id);

                } else if (cmd.equals("showbook")) {
                    return new ShowBook(id);
                    
                } else if (cmd.equals("showpatron")) {
                    return new ShowPatron(id);
            
                } else if (cmd.equals("loanhistory")) {
                    return new ShowLoanHistory(id);
                }
                
             }

             else if (parts.length == 3) {
                int patronID = Integer.parseInt(parts[1]);
                int bookID = Integer.parseInt(parts[2]);

                if (cmd.equals("borrow")) {
                    return new BorrowBook(patronID, bookID);
                } else if (cmd.equals("renew")) {
                    return new RenewBook(patronID, bookID);
                } else if (cmd.equals("return")) {
                    return new ReturnBook(patronID, bookID);
                }
                
             }
            
        } catch (NumberFormatException ex) {

        }

        throw new LibraryException("Invalid command.");
    }
}

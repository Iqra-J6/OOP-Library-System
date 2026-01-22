package bcu.cmp5332.librarysystem.data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Appends loan events (BORROW/RENEW/RETURN) to a history log file.
 */
public class LoanHistoryLogger {

    private static final String RESOURCE = "./resources/data/loan_history.txt";
    private static final String SEP = DataManager.SEPARATOR;

    private static void append(String line) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE, true))) {
            out.println(line);
        }
    }

    public static void logBorrow(int patronId, int bookId, LocalDate startDate, LocalDate dueDate) throws IOException {
        append(patronId + SEP + bookId + SEP + "BORROW" + SEP + startDate + SEP + dueDate);
    }

    public static void logRenew(int patronId, int bookId, LocalDate renewDate, LocalDate newDueDate) throws IOException {
        append(patronId + SEP + bookId + SEP + "RENEW" + SEP + renewDate + SEP + newDueDate);
    }

    public static void logReturn(int patronId, int bookId, LocalDate returnDate) throws IOException {
        // due date unknown/irrelevant on return line, keep blank
        append(patronId + SEP + bookId + SEP + "RETURN" + SEP + returnDate + SEP);
    }
}

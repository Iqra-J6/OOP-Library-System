package bcu.cmp5332.librarysystem.gui;

import bcu.cmp5332.librarysystem.commands.BorrowBook;
import bcu.cmp5332.librarysystem.commands.Command;
import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Patron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class IssueBookWindow extends JFrame implements ActionListener {

    private final MainWindow mw;

    private JTable booksTable;
    private JTable patronsTable;

    private final JButton issueBtn = new JButton("Issue");
    private final JButton cancelBtn = new JButton("Cancel");

    private List<Book> availableBooks;
    private List<Patron> activePatrons;

    public IssueBookWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Issue Book");
        setSize(900, 400);
        setLocationRelativeTo(mw);

        availableBooks = mw.getLibrary().getBooks().stream()
                .filter(b -> !b.isDeleted() && !b.isOnLoan())
                .toList();

        activePatrons = mw.getLibrary().getPatrons().stream()
                .filter(p -> !p.isDeleted())
                .toList();

        String[] bookCols = {"ID", "Title", "Author", "Year"};
        Object[][] bookData = new Object[availableBooks.size()][4];
        for (int i = 0; i < availableBooks.size(); i++) {
            Book b = availableBooks.get(i);
            bookData[i][0] = b.getId();
            bookData[i][1] = b.getTitle();
            bookData[i][2] = b.getAuthor();
            bookData[i][3] = b.getPublicationYear();
        }
        booksTable = new JTable(bookData, bookCols);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] patronCols = {"ID", "Name", "Phone"};
        Object[][] patronData = new Object[activePatrons.size()][3];
        for (int i = 0; i < activePatrons.size(); i++) {
            Patron p = activePatrons.get(i);
            patronData[i][0] = p.getId();
            patronData[i][1] = p.getName();
            patronData[i][2] = p.getPhone();
        }
        patronsTable = new JTable(patronData, patronCols);
        patronsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.add(new JScrollPane(booksTable));
        center.add(new JScrollPane(patronsTable));

        JPanel bottom = new JPanel();
        bottom.add(issueBtn);
        bottom.add(cancelBtn);

        issueBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            setVisible(false);
            return;
        }

        if (e.getSource() == issueBtn) {
            int bookRow = booksTable.getSelectedRow();
            int patronRow = patronsTable.getSelectedRow();

            if (bookRow < 0 || patronRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select 1 book and 1 patron.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookId = (int) booksTable.getValueAt(bookRow, 0);
            int patronId = (int) patronsTable.getValueAt(patronRow, 0);

            try {
                Command cmd = new BorrowBook(patronId, bookId);
                cmd.execute(mw.getLibrary(), LocalDate.now());
                mw.displayBooks();
                setVisible(false);
            } catch (LibraryException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

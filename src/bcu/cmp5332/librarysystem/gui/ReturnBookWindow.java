package bcu.cmp5332.librarysystem.gui;

import bcu.cmp5332.librarysystem.commands.Command;
import bcu.cmp5332.librarysystem.commands.ReturnBook;
import bcu.cmp5332.librarysystem.main.LibraryException;
import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Loan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class ReturnBookWindow extends JFrame implements ActionListener {

    private final MainWindow mw;

    private JTable loanTable;
    private final JButton returnBtn = new JButton("Return");
    private final JButton cancelBtn = new JButton("Cancel");

    private List<Book> onLoanBooks;

    public ReturnBookWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Return Book");
        setSize(800, 350);
        setLocationRelativeTo(mw);

        onLoanBooks = mw.getLibrary().getBooks().stream()
                .filter(b -> !b.isDeleted() && b.isOnLoan())
                .toList();

        String[] cols = {"Book ID", "Title", "Patron ID", "Due Date"};
        Object[][] data = new Object[onLoanBooks.size()][4];

        for (int i = 0; i < onLoanBooks.size(); i++) {
            Book b = onLoanBooks.get(i);
            Loan loan = b.getLoan();
            data[i][0] = b.getId();
            data[i][1] = b.getTitle();
            data[i][2] = loan.getPatron().getId();
            data[i][3] = loan.getDueDate();
        }

        loanTable = new JTable(data, cols);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel bottom = new JPanel();
        bottom.add(returnBtn);
        bottom.add(cancelBtn);

        returnBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(loanTable), BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            setVisible(false);
            return;
        }

        if (e.getSource() == returnBtn) {
            int row = loanTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a loan row.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookId = (int) loanTable.getValueAt(row, 0);
            int patronId = (int) loanTable.getValueAt(row, 2);

            try {
                Command cmd = new ReturnBook(patronId, bookId);
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

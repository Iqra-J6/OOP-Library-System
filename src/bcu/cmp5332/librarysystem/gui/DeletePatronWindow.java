package bcu.cmp5332.librarysystem.gui;

import bcu.cmp5332.librarysystem.main.LibraryException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeletePatronWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField idField = new JTextField();

    private JButton deleteBtn = new JButton("Delete");
    private JButton cancelBtn = new JButton("Cancel");

    public DeletePatronWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Delete Patron");
        setSize(300, 150);

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.add(new JLabel("Patron ID: "));
        topPanel.add(idField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteBtn);
        bottomPanel.add(cancelBtn);

        deleteBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        getContentPane().add(topPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deleteBtn) {
            deletePatron();
        } else if (ae.getSource() == cancelBtn) {
            setVisible(false);
        }
    }

    private void deletePatron() {
        try {
            int patronId = Integer.parseInt(idField.getText());
            mw.getLibrary().softDeletePatron(patronId);
            // once you have displayPatrons(), we’ll refresh that.
            setVisible(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (LibraryException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

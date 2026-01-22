package bcu.cmp5332.librarysystem.gui;

import bcu.cmp5332.librarysystem.commands.AddPatron;
import bcu.cmp5332.librarysystem.commands.Command;
import bcu.cmp5332.librarysystem.main.LibraryException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AddPatronWindow extends JFrame implements ActionListener {

    private final MainWindow mw;

    private final JTextField nameText = new JTextField();
    private final JTextField phoneText = new JTextField();
    private final JTextField emailText = new JTextField();

    private final JButton addBtn = new JButton("Add");
    private final JButton cancelBtn = new JButton("Cancel");

    public AddPatronWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Add a New Patron");
        setSize(320, 200);
        setLocationRelativeTo(mw);

        JPanel topPanel = new JPanel(new GridLayout(4, 2));
        topPanel.add(new JLabel("Name: "));
        topPanel.add(nameText);
        topPanel.add(new JLabel("Phone: "));
        topPanel.add(phoneText);
        topPanel.add(new JLabel("Email: "));
        topPanel.add(emailText);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        bottomPanel.add(new JLabel(" "));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            setVisible(false);
            return;
        }

        if (e.getSource() == addBtn) {
            try {
                String name = nameText.getText().trim();
                String phone = phoneText.getText().trim();
                String email = emailText.getText().trim();

                Command cmd = new AddPatron(name, phone, email);
                cmd.execute(mw.getLibrary(), LocalDate.now());

                // Refresh patron table after add
                mw.displayPatrons();

                setVisible(false);

            } catch (LibraryException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

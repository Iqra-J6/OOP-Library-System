package bcu.cmp5332.librarysystem.gui;

import bcu.cmp5332.librarysystem.model.Book;
import bcu.cmp5332.librarysystem.model.Patron;
import bcu.cmp5332.librarysystem.model.Library;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

public class MainWindow extends JFrame implements ActionListener {

    private JMenuBar menuBar;
    private JMenu adminMenu;
    private JMenu booksMenu;
    private JMenu membersMenu;

    private JMenuItem adminExit;

    private JMenuItem booksView;
    private JMenuItem booksAdd;
    private JMenuItem booksDel;
    private JMenuItem booksIssue;
    private JMenuItem booksReturn;
    private JMenuItem booksRenew;

    private JMenuItem memView;
    private JMenuItem memAdd;
    private JMenuItem memDel;

    private Library library;

    public MainWindow(Library library) {
        this.library = library;
        initialize();
    }

    public Library getLibrary() {
        return library;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // ignore look and feel errors
        }

        setTitle("Library Management System");

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Admin menu
        adminMenu = new JMenu("Admin");
        menuBar.add(adminMenu);

        adminExit = new JMenuItem("Exit");
        adminMenu.add(adminExit);
        adminExit.addActionListener(this);

        // Books menu
        booksMenu = new JMenu("Books");
        menuBar.add(booksMenu);

        booksView = new JMenuItem("View");
        booksAdd = new JMenuItem("Add");
        booksDel = new JMenuItem("Delete");
        booksIssue = new JMenuItem("Issue");
        booksReturn = new JMenuItem("Return");
        booksRenew = new JMenuItem("Renew");

        booksMenu.add(booksView);
        booksMenu.add(booksAdd);
        booksMenu.add(booksDel);
        booksMenu.add(booksIssue);
        booksMenu.add(booksReturn);
        booksMenu.add(booksRenew);

        // Add listeners to all book menu items
        for (int i = 0; i < booksMenu.getItemCount(); i++) {
            booksMenu.getItem(i).addActionListener(this);
        }

        // Patrons menu
        membersMenu = new JMenu("Patrons");
        menuBar.add(membersMenu);

        memView = new JMenuItem("View");
        memAdd = new JMenuItem("Add");
        memDel = new JMenuItem("Delete");

        membersMenu.add(memView);
        membersMenu.add(memAdd);
        membersMenu.add(memDel);

        memView.addActionListener(this);
        memAdd.addActionListener(this);
        memDel.addActionListener(this);

        setSize(800, 500);

        setVisible(true);
        setAutoRequestFocus(true);
        toFront();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* Uncomment the following line to not terminate the console app when the window is closed */
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == adminExit) {
            System.exit(0);

        } else if (ae.getSource() == booksView) {
            displayBooks();

        } else if (ae.getSource() == booksAdd) {
            new AddBookWindow(this);

        } else if (ae.getSource() == booksDel) {
            new DeleteBookWindow(this);

        } else if (ae.getSource() == booksIssue) {
            new IssueBookWindow(this);

        } else if (ae.getSource() == booksReturn) {
            new ReturnBookWindow(this);

        } else if (ae.getSource() == booksRenew) {
            new RenewBookWindow(this);

        } else if (ae.getSource() == memView) {
            // ✅ Step 2: wire Patrons → View
            displayPatrons();

        } else if (ae.getSource() == memAdd) {
            new AddPatronWindow(this);

        } else if (ae.getSource() == memDel) {
            new DeletePatronWindow(this);
        }
    }

    public void displayBooks() {
        List<Book> booksList = library.getBooks();
        String[] columns = new String[]{"Title", "Author", "Pub Date", "Status"};

        Object[][] data = new Object[booksList.size()][4];
        for (int i = 0; i < booksList.size(); i++) {
            Book book = booksList.get(i);
            data[i][0] = book.getTitle();
            data[i][1] = book.getAuthor();
            data[i][2] = book.getPublicationYear();
            data[i][3] = book.getStatus();
        }

        JTable table = new JTable(data, columns);
        this.getContentPane().removeAll();
        this.getContentPane().add(new JScrollPane(table));
        this.revalidate();
    }

    // ✅ Step 3: add displayPatrons() like displayBooks()
    public void displayPatrons() {
        List<Patron> patronsList = library.getPatrons();
        String[] columns = new String[]{"ID", "Name", "Phone"};

        Object[][] data = new Object[patronsList.size()][3];
        for (int i = 0; i < patronsList.size(); i++) {
            Patron patron = patronsList.get(i);
            data[i][0] = patron.getId();
            data[i][1] = patron.getName();
            data[i][2] = patron.getPhone();
        }

        JTable table = new JTable(data, columns);
        this.getContentPane().removeAll();
        this.getContentPane().add(new JScrollPane(table));
        this.revalidate();
    }
}

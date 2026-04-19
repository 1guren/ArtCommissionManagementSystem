package finals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// Observer Pattern: ClientPanel observes commission changes
public class ClientPanel extends JFrame implements CommissionObserver {

    private static final long serialVersionUID = 1L;

    private SystemManager system = SystemManager.getInstance();
    private User loggedInUser;

    // --- Submit Request Tab fields ---
    private JComboBox<String> cbxArtist;
    private JComboBox<String> cbxCategory;
    private JTextArea txtDescription;
    private JTextField txtBudget;
    private JTextField txtDeadline;

    // --- My Commissions Tab ---
    private DefaultTableModel commissionsModel;
    private JTable commissionsTable;

    // --- Browse Artists Tab ---
    private DefaultTableModel artistsModel;
    private JTable artistsTable;

    public ClientPanel() {
        this.loggedInUser = system.getLoggedInUser();

        setTitle("Client Panel - Art Commission Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        // Register as observer
        system.addObserver(this);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Header
        JLabel headerLabel = new JLabel("  Welcome, " + loggedInUser.getFullname() + "! (Client)");
        headerLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        headerLabel.setBackground(new Color(153, 102, 0));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        contentPane.add(headerLabel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab("Submit Commission", buildSubmitTab());
        tabbedPane.addTab("My Commissions", buildMyCommissionsTab());
        tabbedPane.addTab("Browse Artists", buildBrowseArtistsTab());

        // Logout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(180, 0, 0));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    ClientPanel.this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    system.removeObserver(ClientPanel.this);
                    system.logout();
                    dispose();
                    new LoginForm().setVisible(true);
                }
            }
        });
        bottomPanel.add(logoutBtn);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        // Load data
        loadArtistsIntoComboBox();
        loadMyCommissions();
        loadBrowseArtists();
    }

    // -------------------------------------------------------
    // SUBMIT COMMISSION TAB
    // -------------------------------------------------------

    private JPanel buildSubmitTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("Submit a Commission Request");
        title.setFont(new Font("Arial Black", Font.BOLD, 13));
        title.setBounds(280, 25, 300, 22);
        panel.add(title);

        // Artist
        JLabel lblArtist = new JLabel("Select Artist:");
        lblArtist.setFont(new Font("Arial Black", Font.PLAIN, 11));
        lblArtist.setBounds(230, 75, 120, 22);
        panel.add(lblArtist);

        cbxArtist = new JComboBox<>();
        cbxArtist.setBounds(360, 75, 220, 22);
        panel.add(cbxArtist);

        // Category
        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setFont(new Font("Arial Black", Font.PLAIN, 11));
        lblCategory.setBounds(230, 115, 120, 22);
        panel.add(lblCategory);

        cbxCategory = new JComboBox<>(new String[]{"Anime", "Realism", "Digital Art", "Portrait"});
        cbxCategory.setBounds(360, 115, 220, 22);
        panel.add(cbxCategory);

        // Description
        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setFont(new Font("Arial Black", Font.PLAIN, 11));
        lblDescription.setBounds(230, 155, 120, 22);
        panel.add(lblDescription);

        txtDescription = new JTextArea();
        txtDescription.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        descScroll.setBounds(360, 155, 220, 70);
        panel.add(descScroll);

        // Budget
        JLabel lblBudget = new JLabel("Budget (PHP):");
        lblBudget.setFont(new Font("Arial Black", Font.PLAIN, 11));
        lblBudget.setBounds(230, 240, 120, 22);
        panel.add(lblBudget);

        txtBudget = new JTextField();
        txtBudget.setBounds(360, 240, 220, 22);
        panel.add(txtBudget);

        // Deadline
        JLabel lblDeadline = new JLabel("Deadline:");
        lblDeadline.setFont(new Font("Arial Black", Font.PLAIN, 11));
        lblDeadline.setBounds(230, 280, 120, 22);
        panel.add(lblDeadline);

        txtDeadline = new JTextField("YYYY-MM-DD");
        txtDeadline.setBounds(360, 280, 220, 22);
        panel.add(txtDeadline);

        // Submit button
        JButton submitBtn = new JButton("Submit Request");
        submitBtn.setBounds(360, 325, 220, 30);
        submitBtn.setBackground(new Color(0, 102, 153));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitRequest();
            }
        });
        panel.add(submitBtn);

        return panel;
    }

    private void loadArtistsIntoComboBox() {
        cbxArtist.removeAllItems();
        for (User u : system.getArtists()) {
            cbxArtist.addItem(u.getUsername() + " - " + u.getFullname());
        }
    }

    private void submitRequest() {
        String artistItem = (String) cbxArtist.getSelectedItem();
        String category = (String) cbxCategory.getSelectedItem();
        String description = txtDescription.getText().trim();
        String budgetText = txtBudget.getText().trim();
        String deadline = txtDeadline.getText().trim();

        if (artistItem == null || description.isEmpty() || budgetText.isEmpty() || deadline.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double budget;
        try {
            budget = Double.parseDouble(budgetText);
            if (budget <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid budget amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract just the username from "username - Full Name"
        String artistUsername = artistItem.split(" - ")[0];

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Submit commission to \"" + artistUsername + "\"?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            CommissionRequest request = new CommissionRequest(
                loggedInUser.getUsername(),
                artistUsername,
                category,
                description,
                budget,
                deadline
            );
            system.addCommission(request); // this also notifies observers

            // Clear form
            txtDescription.setText("");
            txtBudget.setText("");
            txtDeadline.setText("YYYY-MM-DD");

            loadMyCommissions();
            JOptionPane.showMessageDialog(this, "Commission request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // -------------------------------------------------------
    // MY COMMISSIONS TAB
    // -------------------------------------------------------

    private JPanel buildMyCommissionsTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("My Commission Requests");
        title.setFont(new Font("Arial Black", Font.BOLD, 13));
        title.setBounds(10, 10, 300, 22);
        panel.add(title);

        String[] columns = {"Artist", "Category", "Description", "Budget", "Deadline", "Status"};
        commissionsModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        commissionsTable = new JTable(commissionsModel);

        JScrollPane scrollPane = new JScrollPane(commissionsTable);
        scrollPane.setBounds(10, 40, 855, 360);
        panel.add(scrollPane);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(10, 415, 100, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMyCommissions();
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    public void loadMyCommissions() {
        commissionsModel.setRowCount(0);
        for (CommissionRequest req : system.getCommissionsByClient(loggedInUser.getUsername())) {
            commissionsModel.addRow(new Object[]{
                req.getArtistUsername(),
                req.getCategory(),
                req.getDescription(),
                "PHP " + req.getBudget(),
                req.getDeadline(),
                req.getStatus()
            });
        }
    }

    // -------------------------------------------------------
    // BROWSE ARTISTS TAB
    // -------------------------------------------------------

    private JPanel buildBrowseArtistsTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("Browse Artists & Their Portfolios");
        title.setFont(new Font("Arial Black", Font.BOLD, 13));
        title.setBounds(10, 10, 350, 22);
        panel.add(title);

        String[] columns = {"Artist Username", "Full Name", "Number of Artworks"};
        artistsModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        artistsTable = new JTable(artistsModel);

        JScrollPane scrollPane = new JScrollPane(artistsTable);
        scrollPane.setBounds(10, 40, 855, 360);
        panel.add(scrollPane);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(10, 415, 100, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadBrowseArtists();
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    private void loadBrowseArtists() {
        artistsModel.setRowCount(0);
        for (User u : system.getArtists()) {
            int artworkCount = system.getArtworksByArtist(u.getUsername()).size();
            artistsModel.addRow(new Object[]{
                u.getUsername(),
                u.getFullname(),
                artworkCount
            });
        }
    }

    // -------------------------------------------------------
    // Observer Pattern: auto-refresh when commissions change
    // -------------------------------------------------------

    public void onCommissionUpdated(CommissionRequest request) {
        // Only refresh if this commission belongs to the logged-in client
        if (request.getClientUsername().equals(loggedInUser.getUsername())) {
            loadMyCommissions();
        }
    }
}
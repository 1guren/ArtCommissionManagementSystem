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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// Observer Pattern: ArtistPanel observes commission changes
public class ArtistPanel extends JFrame implements CommissionObserver {

    private static final long serialVersionUID = 1L;

    private SystemManager system = SystemManager.getInstance();
    private User loggedInUser;

    // --- My Portfolio Tab ---
    private DefaultTableModel portfolioModel;
    private JTable portfolioTable;

    // --- Commission Requests Tab ---
    private DefaultTableModel commissionsModel;
    private JTable commissionsTable;

    public ArtistPanel() {
        this.loggedInUser = system.getLoggedInUser();

        setTitle("Artist Panel - Art Commission Management System");
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
        JLabel headerLabel = new JLabel("  Welcome, " + loggedInUser.getFullname() + "! (Artist)");
        headerLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        headerLabel.setBackground(new Color(102, 51, 153));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        contentPane.add(headerLabel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab("My Portfolio", buildPortfolioTab());
        tabbedPane.addTab("Commission Requests", buildCommissionsTab());
        tabbedPane.addTab("Add Artwork", buildAddArtworkTab());

        // Logout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(180, 0, 0));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    ArtistPanel.this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    system.removeObserver(ArtistPanel.this);
                    system.logout();
                    dispose();
                    new LoginForm().setVisible(true);
                }
            }
        });
        bottomPanel.add(logoutBtn);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        // Load all data
        loadPortfolio();
        loadCommissions();
    }

    // -------------------------------------------------------
    // MY PORTFOLIO TAB
    // -------------------------------------------------------

    private JPanel buildPortfolioTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("My Artworks");
        title.setFont(new Font("Arial Black", Font.BOLD, 13));
        title.setBounds(10, 10, 200, 22);
        panel.add(title);

        String[] columns = {"Title", "Category"};
        portfolioModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        portfolioTable = new JTable(portfolioModel);

        JScrollPane scrollPane = new JScrollPane(portfolioTable);
        scrollPane.setBounds(10, 40, 855, 320);
        panel.add(scrollPane);

        // Delete Artwork button
        JButton deleteBtn = new JButton("Delete Selected Artwork");
        deleteBtn.setBounds(10, 375, 200, 28);
        deleteBtn.setBackground(new Color(180, 0, 0));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedArtwork();
            }
        });
        panel.add(deleteBtn);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(225, 375, 100, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPortfolio();
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    private void loadPortfolio() {
        portfolioModel.setRowCount(0);
        for (Artwork art : system.getArtworksByArtist(loggedInUser.getUsername())) {
            portfolioModel.addRow(new Object[]{
                art.getTitle(),
                art.getCategory()
            });
        }
    }

    private void deleteSelectedArtwork() {
        int row = portfolioTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an artwork first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String artTitle = (String) portfolioModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete artwork \"" + artTitle + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            system.removeArtwork(artTitle, loggedInUser.getUsername());
            loadPortfolio();
            JOptionPane.showMessageDialog(this, "Artwork deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // -------------------------------------------------------
    // COMMISSION REQUESTS TAB
    // -------------------------------------------------------

    private JPanel buildCommissionsTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("Commission Requests for Me");
        title.setFont(new Font("Arial Black", Font.BOLD, 13));
        title.setBounds(10, 10, 300, 22);
        panel.add(title);

        String[] columns = {"Client", "Category", "Description", "Budget", "Deadline", "Status"};
        commissionsModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        commissionsTable = new JTable(commissionsModel);

        JScrollPane scrollPane = new JScrollPane(commissionsTable);
        scrollPane.setBounds(10, 40, 855, 310);
        panel.add(scrollPane);

        // Accept button
        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setBounds(10, 365, 120, 28);
        acceptBtn.setBackground(new Color(51, 153, 102));
        acceptBtn.setForeground(Color.WHITE);
        acceptBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        acceptBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSelectedCommissionStatus("Accepted");
            }
        });
        panel.add(acceptBtn);

        // Decline button
        JButton declineBtn = new JButton("Decline");
        declineBtn.setBounds(145, 365, 120, 28);
        declineBtn.setBackground(new Color(180, 0, 0));
        declineBtn.setForeground(Color.WHITE);
        declineBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        declineBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSelectedCommissionStatus("Declined");
            }
        });
        panel.add(declineBtn);

        // Mark as Completed button
        JButton completeBtn = new JButton("Mark as Completed");
        completeBtn.setBounds(280, 365, 180, 28);
        completeBtn.setBackground(new Color(0, 102, 153));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        completeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSelectedCommissionStatus("Completed");
            }
        });
        panel.add(completeBtn);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(475, 365, 100, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadCommissions();
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    private void loadCommissions() {
        commissionsModel.setRowCount(0);
        for (CommissionRequest req : system.getCommissionsByArtist(loggedInUser.getUsername())) {
            commissionsModel.addRow(new Object[]{
                req.getClientUsername(),
                req.getCategory(),
                req.getDescription(),
                req.getBudget(),
                req.getDeadline(),
                req.getStatus()
            });
        }
    }

    private void updateSelectedCommissionStatus(String newStatus) {
        int row = commissionsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a commission first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String clientUsername = (String) commissionsModel.getValueAt(row, 0);
        String category = (String) commissionsModel.getValueAt(row, 1);

        // Find the matching commission in the system
        CommissionRequest target = null;
        for (CommissionRequest req : system.getCommissionsByArtist(loggedInUser.getUsername())) {
            if (req.getClientUsername().equals(clientUsername) && req.getCategory().equals(category)) {
                target = req;
                break;
            }
        }

        if (target != null) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Mark this commission as \"" + newStatus + "\"?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                system.updateCommissionStatus(target, newStatus); // also notifies observers
                loadCommissions();
                JOptionPane.showMessageDialog(this, "Commission status updated to: " + newStatus, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // -------------------------------------------------------
    // ADD ARTWORK TAB
    // -------------------------------------------------------

    private JPanel buildAddArtworkTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("Add New Artwork to Portfolio");
        title.setFont(new Font("Arial Black", Font.BOLD, 13));
        title.setBounds(280, 30, 300, 22);
        panel.add(title);

        JLabel lblTitle = new JLabel("Artwork Title:");
        lblTitle.setFont(new Font("Arial Black", Font.PLAIN, 11));
        lblTitle.setBounds(240, 80, 120, 22);
        panel.add(lblTitle);

        JTextField txtTitle = new JTextField();
        txtTitle.setBounds(370, 80, 220, 22);
        panel.add(txtTitle);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setFont(new Font("Arial Black", Font.PLAIN, 11));
        lblCategory.setBounds(240, 120, 120, 22);
        panel.add(lblCategory);

        JComboBox<String> cbxCategory = new JComboBox<>(new String[]{"Anime", "Realism", "Digital Art", "Portrait"});
        cbxCategory.setBounds(370, 120, 220, 22);
        panel.add(cbxCategory);

        JButton addBtn = new JButton("Add Artwork");
        addBtn.setBounds(370, 170, 220, 30);
        addBtn.setBackground(new Color(51, 153, 102));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String artTitle = txtTitle.getText().trim();
                String category = (String) cbxCategory.getSelectedItem();

                if (artTitle.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please enter a title.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    ArtistPanel.this,
                    "Add \"" + artTitle + "\" to your portfolio?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    Artwork newArt = new Artwork(artTitle, category, loggedInUser.getUsername());
                    system.addArtwork(newArt);
                    txtTitle.setText("");
                    loadPortfolio(); // refresh portfolio tab too
                    JOptionPane.showMessageDialog(ArtistPanel.this, "Artwork added to your portfolio!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel.add(addBtn);

        return panel;
    }

    // -------------------------------------------------------
    // Observer Pattern: auto-refresh when commissions change
    // -------------------------------------------------------

    public void onCommissionUpdated(CommissionRequest request) {
        loadCommissions();
    }
}
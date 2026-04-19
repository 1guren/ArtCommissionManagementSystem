package finals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// Observer Pattern: AdminPanel observes commission changes
public class AdminPanel extends JFrame implements CommissionObserver {

    private static final long serialVersionUID = 1L;

    private SystemManager system = SystemManager.getInstance();

    // --- Users Tab ---
    private DefaultTableModel usersModel;
    private JTable usersTable;

    // --- Artworks Tab ---
    private DefaultTableModel artworksModel;
    private JTable artworksTable;

    // --- Commissions Tab ---
    private DefaultTableModel commissionsModel;
    private JTable commissionsTable;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminPanel frame = new AdminPanel();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AdminPanel() {
        setTitle("Admin Panel - Art Commission Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        // Register as observer so commissions tab auto-refreshes
        system.addObserver(this);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Header
        JLabel headerLabel = new JLabel("  Welcome, Admin!");
        headerLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        headerLabel.setBackground(new Color(0, 102, 153));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBounds(0, 0, 900, 35);
        contentPane.add(headerLabel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab("Manage Users", buildUsersTab());
        tabbedPane.addTab("Manage Artworks", buildArtworksTab());
        tabbedPane.addTab("Manage Commissions", buildCommissionsTab());
        tabbedPane.addTab("Reports", new ReportsPanel());

        // Logout button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(180, 0, 0));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    AdminPanel.this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    system.removeObserver(AdminPanel.this);
                    system.logout();
                    dispose();
                    new LoginForm().setVisible(true);
                }
            }
        });
        bottomPanel.add(logoutBtn);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        // Load data into all tables
        loadUsers();
        loadArtworks();
        loadCommissions();
    }

    // -------------------------------------------------------
    // MANAGE USERS TAB
    // -------------------------------------------------------

    private JPanel buildUsersTab() {
        JPanel panel = new JPanel(null);

        String[] columns = {"Full Name", "Username", "Role"};
        usersModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        usersTable = new JTable(usersModel);

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBounds(10, 10, 855, 330);
        panel.add(scrollPane);

        // Add User button
        JButton addBtn = new JButton("Add User");
        addBtn.setBounds(10, 355, 130, 28);
        addBtn.setBackground(new Color(51, 153, 102));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddUserDialog();
            }
        });
        panel.add(addBtn);

        // Delete User button
        JButton deleteBtn = new JButton("Delete User");
        deleteBtn.setBounds(155, 355, 130, 28);
        deleteBtn.setBackground(new Color(180, 0, 0));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedUser();
            }
        });
        panel.add(deleteBtn);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(300, 355, 100, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    private void loadUsers() {
        usersModel.setRowCount(0);
        for (User user : system.getUsers()) {
            usersModel.addRow(new Object[]{
                user.getFullname(),
                user.getUsername(),
                user.getRole()
            });
        }
    }

    private void showAddUserDialog() {
        JPanel dialogPanel = new JPanel(null);
        dialogPanel.setPreferredSize(new java.awt.Dimension(280, 180));

        JLabel lbl1 = new JLabel("Full Name:");
        lbl1.setBounds(10, 10, 80, 22);
        dialogPanel.add(lbl1);

        JTextField txtFullname = new JTextField();
        txtFullname.setBounds(100, 10, 160, 22);
        dialogPanel.add(txtFullname);

        JLabel lbl2 = new JLabel("Username:");
        lbl2.setBounds(10, 45, 80, 22);
        dialogPanel.add(lbl2);

        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(100, 45, 160, 22);
        dialogPanel.add(txtUsername);

        JLabel lbl3 = new JLabel("Password:");
        lbl3.setBounds(10, 80, 80, 22);
        dialogPanel.add(lbl3);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 80, 160, 22);
        dialogPanel.add(txtPassword);

        JLabel lbl4 = new JLabel("Role:");
        lbl4.setBounds(10, 115, 80, 22);
        dialogPanel.add(lbl4);

        JComboBox<String> cbxRole = new JComboBox<>(new String[]{"Client", "Artist"});
        cbxRole.setBounds(100, 115, 160, 22);
        dialogPanel.add(cbxRole);

        int result = JOptionPane.showConfirmDialog(
            this, dialogPanel, "Add New User", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String fullname = txtFullname.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String role = (String) cbxRole.getSelectedItem();

            if (fullname.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (system.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already taken.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = UserFactory.createUser(role, fullname, username, password);
            system.addUser(newUser);
            loadUsers();
            JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        int row = usersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) usersModel.getValueAt(row, 1);
        String role = (String) usersModel.getValueAt(row, 2);

        if (role.equalsIgnoreCase("Admin")) {
            JOptionPane.showMessageDialog(this, "Cannot delete an Admin account.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete user \"" + username + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            system.removeUser(username);
            loadUsers();
            JOptionPane.showMessageDialog(this, "User deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // -------------------------------------------------------
    // MANAGE ARTWORKS TAB
    // -------------------------------------------------------

    private JPanel buildArtworksTab() {
        JPanel panel = new JPanel(null);

        String[] columns = {"Title", "Category", "Artist"};
        artworksModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        artworksTable = new JTable(artworksModel);

        JScrollPane scrollPane = new JScrollPane(artworksTable);
        scrollPane.setBounds(10, 10, 855, 330);
        panel.add(scrollPane);

        // Add Artwork button
        JButton addBtn = new JButton("Add Artwork");
        addBtn.setBounds(10, 355, 140, 28);
        addBtn.setBackground(new Color(51, 153, 102));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddArtworkDialog();
            }
        });
        panel.add(addBtn);

        // Delete Artwork button
        JButton deleteBtn = new JButton("Delete Artwork");
        deleteBtn.setBounds(165, 355, 140, 28);
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
        refreshBtn.setBounds(320, 355, 100, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadArtworks();
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    private void loadArtworks() {
        artworksModel.setRowCount(0);
        for (Artwork art : system.getArtworks()) {
            artworksModel.addRow(new Object[]{
                art.getTitle(),
                art.getCategory(),
                art.getArtistUsername()
            });
        }
    }

    private void showAddArtworkDialog() {
        JPanel dialogPanel = new JPanel(null);
        dialogPanel.setPreferredSize(new java.awt.Dimension(280, 140));

        JLabel lbl1 = new JLabel("Title:");
        lbl1.setBounds(10, 10, 80, 22);
        dialogPanel.add(lbl1);

        JTextField txtTitle = new JTextField();
        txtTitle.setBounds(100, 10, 160, 22);
        dialogPanel.add(txtTitle);

        JLabel lbl2 = new JLabel("Category:");
        lbl2.setBounds(10, 45, 80, 22);
        dialogPanel.add(lbl2);

        JComboBox<String> cbxCategory = new JComboBox<>(new String[]{"Anime", "Realism", "Digital Art", "Portrait"});
        cbxCategory.setBounds(100, 45, 160, 22);
        dialogPanel.add(cbxCategory);

        JLabel lbl3 = new JLabel("Artist:");
        lbl3.setBounds(10, 80, 80, 22);
        dialogPanel.add(lbl3);

        JComboBox<String> cbxArtist = new JComboBox<>();
        for (User u : system.getArtists()) {
            cbxArtist.addItem(u.getUsername());
        }
        cbxArtist.setBounds(100, 80, 160, 22);
        dialogPanel.add(cbxArtist);

        int result = JOptionPane.showConfirmDialog(
            this, dialogPanel, "Add Artwork", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String title = txtTitle.getText().trim();
            String category = (String) cbxCategory.getSelectedItem();
            String artist = (String) cbxArtist.getSelectedItem();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Artwork newArt = new Artwork(title, category, artist);
            system.addArtwork(newArt);
            loadArtworks();
            JOptionPane.showMessageDialog(this, "Artwork added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedArtwork() {
        int row = artworksTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an artwork first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = (String) artworksModel.getValueAt(row, 0);
        String artist = (String) artworksModel.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete artwork \"" + title + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            system.removeArtwork(title, artist);
            loadArtworks();
            JOptionPane.showMessageDialog(this, "Artwork deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // -------------------------------------------------------
    // MANAGE COMMISSIONS TAB
    // -------------------------------------------------------

    private JPanel buildCommissionsTab() {
        JPanel panel = new JPanel(null);

        String[] columns = {"Client", "Artist", "Category", "Budget", "Deadline", "Status"};
        commissionsModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        commissionsTable = new JTable(commissionsModel);

        JScrollPane scrollPane = new JScrollPane(commissionsTable);
        scrollPane.setBounds(10, 10, 855, 330);
        panel.add(scrollPane);

        // Delete Commission button
        JButton deleteBtn = new JButton("Delete Commission");
        deleteBtn.setBounds(10, 355, 160, 28);
        deleteBtn.setBackground(new Color(180, 0, 0));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCommission();
            }
        });
        panel.add(deleteBtn);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(185, 355, 100, 28);
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
        for (CommissionRequest req : system.getCommissions()) {
            commissionsModel.addRow(new Object[]{
                req.getClientUsername(),
                req.getArtistUsername(),
                req.getCategory(),
                req.getBudget(),
                req.getDeadline(),
                req.getStatus()
            });
        }
    }

    private void deleteSelectedCommission() {
        int row = commissionsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a commission first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String client = (String) commissionsModel.getValueAt(row, 0);
        String artist = (String) commissionsModel.getValueAt(row, 1);
        String category = (String) commissionsModel.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete commission from \"" + client + "\" to \"" + artist + "\" (" + category + ")?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Find the matching CommissionRequest object and remove it
            CommissionRequest toRemove = null;
            for (CommissionRequest req : system.getCommissions()) {
                if (req.getClientUsername().equals(client)
                        && req.getArtistUsername().equals(artist)
                        && req.getCategory().equals(category)) {
                    toRemove = req;
                    break;
                }
            }
            if (toRemove != null) {
                system.removeCommission(toRemove);
                loadCommissions();
                JOptionPane.showMessageDialog(this, "Commission deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // -------------------------------------------------------
    // Observer Pattern: called automatically when commissions change
    // -------------------------------------------------------

    public void onCommissionUpdated(CommissionRequest request) {
        loadCommissions(); // auto-refresh commissions table
    }
}
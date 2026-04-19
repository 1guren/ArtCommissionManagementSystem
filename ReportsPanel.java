package finals;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;

public class ReportsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private SystemManager system = SystemManager.getInstance();

    // --- Report 1: Commission Activity ---
    private DefaultTableModel activityModel;

    // --- Report 2: Most Requested Categories ---
    private DefaultTableModel categoryModel;

    // --- Report 3: Client Commission History ---
    private DefaultTableModel historyModel;

    public ReportsPanel() {
        setLayout(null);

        // Title
        JLabel title = new JLabel("Reports");
        title.setFont(new Font("Arial Black", Font.BOLD, 15));
        title.setBounds(10, 10, 200, 25);
        add(title);

        // Inner tabbed pane for the 3 reports
        JTabbedPane reportTabs = new JTabbedPane();
        reportTabs.setBounds(10, 45, 855, 450);
        add(reportTabs);

        reportTabs.addTab("Commission Activity", buildActivityTab());
        reportTabs.addTab("Most Requested Categories", buildCategoryTab());
        reportTabs.addTab("Client Commission History", buildHistoryTab());

        // Load all reports on open
        loadActivityReport();
        loadCategoryReport();
        loadHistoryReport();
    }

    // -------------------------------------------------------
    // REPORT 1: COMMISSION ACTIVITY REPORT
    // Shows all commissions with their full details and status
    // -------------------------------------------------------

    private JPanel buildActivityTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("Full Commission Activity Report");
        title.setFont(new Font("Arial Black", Font.BOLD, 12));
        title.setBounds(10, 10, 350, 22);
        panel.add(title);

        JLabel subtitle = new JLabel("Shows all commissions in the system and their current status.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitle.setBounds(10, 32, 500, 18);
        panel.add(subtitle);

        String[] columns = {"Client", "Artist", "Category", "Budget (PHP)", "Deadline", "Status"};
        activityModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable activityTable = new JTable(activityModel);
        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setBounds(10, 58, 830, 300);
        panel.add(scrollPane);

        // Summary labels (Total, Pending, Accepted, etc.)
        JLabel lblSummary = new JLabel("Summary:");
        lblSummary.setFont(new Font("Arial Black", Font.BOLD, 11));
        lblSummary.setBounds(10, 368, 100, 20);
        panel.add(lblSummary);

        JLabel lblCounts = new JLabel("");
        lblCounts.setFont(new Font("Arial", Font.PLAIN, 11));
        lblCounts.setBounds(110, 368, 600, 20);
        panel.add(lblCounts);

        JButton refreshBtn = new JButton("Refresh Report");
        refreshBtn.setBounds(10, 395, 150, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.setBackground(new Color(0, 102, 153));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadActivityReport();

                // Count each status using simple loops
                int total = system.getCommissions().size();
                int pending = 0;
                int accepted = 0;
                int declined = 0;
                int completed = 0;

                for (CommissionRequest req : system.getCommissions()) {
                    if (req.getStatus().equals("Pending"))   pending++;
                    if (req.getStatus().equals("Accepted"))  accepted++;
                    if (req.getStatus().equals("Declined"))  declined++;
                    if (req.getStatus().equals("Completed")) completed++;
                }

                lblCounts.setText(
                    "Total: " + total +
                    "  |  Pending: " + pending +
                    "  |  Accepted: " + accepted +
                    "  |  Declined: " + declined +
                    "  |  Completed: " + completed
                );
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    private void loadActivityReport() {
        activityModel.setRowCount(0);
        for (CommissionRequest req : system.getCommissions()) {
            activityModel.addRow(new Object[]{
                req.getClientUsername(),
                req.getArtistUsername(),
                req.getCategory(),
                req.getBudget(),
                req.getDeadline(),
                req.getStatus()
            });
        }
    }

    // -------------------------------------------------------
    // REPORT 2: MOST REQUESTED ART CATEGORIES
    // Counts how many times each category was requested
    // Uses simple if-else counting — no advanced code
    // -------------------------------------------------------

    private JPanel buildCategoryTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("Most Requested Art Categories");
        title.setFont(new Font("Arial Black", Font.BOLD, 12));
        title.setBounds(10, 10, 350, 22);
        panel.add(title);

        JLabel subtitle = new JLabel("Shows how many commission requests were made per art category.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitle.setBounds(10, 32, 500, 18);
        panel.add(subtitle);

        String[] columns = {"Art Category", "Number of Requests", "Rank"};
        categoryModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable categoryTable = new JTable(categoryModel);
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBounds(10, 58, 830, 300);
        panel.add(scrollPane);

        JButton refreshBtn = new JButton("Refresh Report");
        refreshBtn.setBounds(10, 370, 150, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.setBackground(new Color(0, 102, 153));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadCategoryReport();
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    private void loadCategoryReport() {
        categoryModel.setRowCount(0);

        // Count each category using simple if-else — no HashMap, no advanced code
        int animeCount = 0;
        int realismCount = 0;
        int digitalCount = 0;
        int portraitCount = 0;

        for (CommissionRequest req : system.getCommissions()) {
            if (req.getCategory().equals("Anime")) {
                animeCount++;
            } else if (req.getCategory().equals("Realism")) {
                realismCount++;
            } else if (req.getCategory().equals("Digital Art")) {
                digitalCount++;
            } else if (req.getCategory().equals("Portrait")) {
                portraitCount++;
            }
        }

        // Store counts in simple arrays so we can sort them (bubble sort)
        String[] categories = {"Anime", "Realism", "Digital Art", "Portrait"};
        int[] counts = {animeCount, realismCount, digitalCount, portraitCount};

        // Bubble sort — sort by count descending (highest first)
        for (int i = 0; i < categories.length - 1; i++) {
            for (int j = 0; j < categories.length - 1 - i; j++) {
                if (counts[j] < counts[j + 1]) {
                    // Swap counts
                    int tempCount = counts[j];
                    counts[j] = counts[j + 1];
                    counts[j + 1]  = tempCount;
                    // Swap category names too
                    String tempCategory = categories[j];
                    categories[j] = categories[j + 1];
                    categories[j + 1] = tempCategory;
                }
            }
        }

        // Add rows with rank number
        for (int i = 0; i < categories.length; i++) {
            categoryModel.addRow(new Object[]{
                categories[i],
                counts[i],
                "#" + (i + 1)
            });
        }
    }

    // -------------------------------------------------------
    // REPORT 3: CLIENT COMMISSION HISTORY
    // Shows each client and all their commission requests
    // -------------------------------------------------------

    private JPanel buildHistoryTab() {
        JPanel panel = new JPanel(null);

        JLabel title = new JLabel("Client Commission History");
        title.setFont(new Font("Arial Black", Font.BOLD, 12));
        title.setBounds(10, 10, 350, 22);
        panel.add(title);

        JLabel subtitle = new JLabel("Shows each client and all their commission requests.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitle.setBounds(10, 32, 500, 18);
        panel.add(subtitle);

        String[] columns = {"Client", "Artist", "Category", "Budget (PHP)", "Deadline", "Status"};
        historyModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable historyTable = new JTable(historyModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBounds(10, 58, 830, 300);
        panel.add(scrollPane);

        JButton refreshBtn = new JButton("Refresh Report");
        refreshBtn.setBounds(10, 370, 150, 28);
        refreshBtn.setFont(new Font("Arial Black", Font.BOLD, 11));
        refreshBtn.setBackground(new Color(0, 102, 153));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadHistoryReport();
            }
        });
        panel.add(refreshBtn);

        return panel;
    }

    private void loadHistoryReport() {
        historyModel.setRowCount(0);

        // Loop through each client, then get their commissions
        // This groups the table by client
        for (User user : system.getUsers()) {
            if (user.getRole().equalsIgnoreCase("Client")) {

                // Get all commissions for this client
                for (CommissionRequest req : system.getCommissionsByClient(user.getUsername())) {
                    historyModel.addRow(new Object[]{
                        req.getClientUsername(),
                        req.getArtistUsername(),
                        req.getCategory(),
                        req.getBudget(),
                        req.getDeadline(),
                        req.getStatus()
                    });
                }

                // Add a blank separator row between clients for readability
                if (system.getCommissionsByClient(user.getUsername()).size() > 0) {
                    historyModel.addRow(new Object[]{"", "", "", "", "", ""});
                }
            }
        }
    }
}
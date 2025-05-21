// Filename: ExpenseTrackerDashboard.java
import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.table.DefaultTableModel;

import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

public class ExpenseTrackerDashboard extends JFrame {
    private String username;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Connection conn;

    public ExpenseTrackerDashboard(String username) {
        this.username = username;
        connectToDB();
    
        setTitle("Expense Tracker Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    
// === Side Menu ===
JPanel sideMenu = new JPanel();
sideMenu.setLayout(new GridLayout(7, 1, 10, 10));
sideMenu.setBackground(new Color(25, 25, 25));
sideMenu.setPreferredSize(new Dimension(220, getHeight()));
sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

sideMenu.add(createSideButton("🏠 Dashboard", "Dashboard"));
sideMenu.add(createSideButton("➕ Add Expense", "Add Expense"));
sideMenu.add(createSideButton("💰 Add Income", "Add Income"));
sideMenu.add(createSideButton("📜 Transaction History", "Transaction History"));
sideMenu.add(createSideButton("📊 Budget", "Budget"));
sideMenu.add(createSideButton("📈 Charts", "Charts"));
sideMenu.add(createSideButton("❌ Exit", "Exit"));

add(sideMenu, BorderLayout.WEST);

// === Top Bar ===
JPanel topBar = new JPanel(new BorderLayout());
topBar.setBackground(new Color(40, 40, 40));
topBar.setPreferredSize(new Dimension(getWidth(), 60));

JLabel welcomeLabel = new JLabel("  Welcome, " + username );
welcomeLabel.setForeground(Color.WHITE);
welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
topBar.add(welcomeLabel, BorderLayout.WEST);

// Optional Alert/Tip Panel on Top Right
JLabel alertLabel = new JLabel(" Tip: Track expenses daily!");
alertLabel.setForeground(new Color(200, 200, 0));
alertLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
topBar.add(alertLabel, BorderLayout.EAST);

add(topBar, BorderLayout.NORTH);

    
        // === Main Panel with Cards ===
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(245, 245, 245)); // Light background for content
    
        mainPanel.add(createDashboardPanel(), "Dashboard");
        mainPanel.add(createAddExpensePanel(), "Add Expense");
        mainPanel.add(createAddIncomePanel(), "Add Income");
        mainPanel.add(createTransactionPanel(), "Transaction History");
        mainPanel.add(createBudgetPanel(), "Budget");
        mainPanel.add(createChartPanel(), "Charts");
    
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "Dashboard");
    
        setVisible(true);
    }

    private void connectToDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exp_track", "root", "priya@2004");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB Connection Failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private JButton createSideButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(e -> {
            if (panelName.equals("Exit")) System.exit(0);
            else cardLayout.show(mainPanel, panelName);
        });
        return button;
    }

private JPanel createDashboardPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.setBackground(new Color(245, 245, 245)); // Light, clean background

    // Dynamic Greeting based on Time of Day
    String timeOfDay = getTimeOfDay();  // "Morning", "Afternoon", "Evening"
    JLabel welcomeLabel = new JLabel("Good " + timeOfDay + ", " + username + "!", JLabel.LEFT);
    welcomeLabel.setFont(new Font("Roboto", Font.BOLD, 26));  // Modern font
    welcomeLabel.setForeground(new Color(60, 60, 60));  // Dark gray color for text

    // Avatar Icon (circular avatar with a soft border)

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setOpaque(false);

    // Left panel with Avatar and Welcome label
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftPanel.setOpaque(false);
   
    leftPanel.add(welcomeLabel);

    topPanel.add(leftPanel, BorderLayout.WEST);

    // Balance Display – Clean, bold text, minimal style
    double currentBalance = getCurrentBalance();
    JLabel balanceLabel = new JLabel("₹" + String.format("%.2f", currentBalance));
    balanceLabel.setFont(new Font("Roboto", Font.BOLD, 36));  // Large, bold font for balance
    balanceLabel.setForeground(new Color(34, 139, 34));  // Green color for balance
    balanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding around label
    balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // Card-style container for balance (rounded corners, soft shadow)
    JPanel balancePanel = new JPanel(new BorderLayout());
    balancePanel.setOpaque(true);
    balancePanel.setBackground(Color.WHITE);
    balancePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    balancePanel.setPreferredSize(new Dimension(250, 120));  // Size of the balance card
    balancePanel.add(balanceLabel, BorderLayout.CENTER);

    // Add the balance card to the top panel
    topPanel.add(balancePanel, BorderLayout.CENTER);

    panel.add(topPanel, BorderLayout.NORTH);

    return panel;
}

// Helper method to get time of day for dynamic greeting
private String getTimeOfDay() {
    int hour = LocalTime.now().getHour();
    if (hour >= 5 && hour < 12) {
        return "Morning";
    } else if (hour >= 12 && hour < 17) {
        return "Afternoon";
    } else {
        return "Evening";
    }
}

    // ==== 1) BUDGET PANEL ====
    private JPanel createBudgetPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("📅 Monthly Budget Planner"));
    
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Month field defaults to current YYYY-MM
        JTextField monthField = new JTextField(LocalDate.now().toString().substring(0, 7));
        JTextField budgetAmountField = new JTextField();
        JLabel usedLabel      = new JLabel("₹0.00");
        JLabel remainingLabel = new JLabel("₹0.00");
        JProgressBar budgetBar = new JProgressBar(0, 100);
        budgetBar.setStringPainted(true);
        JButton saveBtn       = new JButton("💾 Save/Update Budget");
    
        // Layout
        formPanel.add(new JLabel("Month (YYYY-MM):"));
        formPanel.add(monthField);
        formPanel.add(new JLabel("Budget Amount (₹):"));
        formPanel.add(budgetAmountField);
        formPanel.add(new JLabel("Used:"));
        formPanel.add(usedLabel);
        formPanel.add(new JLabel("Remaining:"));
        formPanel.add(remainingLabel);
        formPanel.add(new JLabel("Progress:"));
        formPanel.add(budgetBar);
        formPanel.add(new JLabel());
        formPanel.add(saveBtn);
    
        panel.add(formPanel);
    
        // Load when month field loses focus
        monthField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                loadBudgetForMonth(
                    monthField.getText().trim(),
                    budgetAmountField, usedLabel, remainingLabel, budgetBar
                );
            }
        });
    
        // Save / Update action
        saveBtn.addActionListener(e -> {
            String month = monthField.getText().trim();
            String budgetText = budgetAmountField.getText().trim();
    
            if (!month.matches("\\d{4}-\\d{2}")) {
                JOptionPane.showMessageDialog(panel,
                    "❌ Invalid month format. Use YYYY-MM.");
                return;
            }
    
            try {
                double budget = Double.parseDouble(budgetText);
    
                // upsert into budgets table
                PreparedStatement check = conn.prepareStatement(
                    "SELECT 1 FROM budgets WHERE username = ? AND month = ?");
                check.setString(1, username);
                check.setString(2, month);
                ResultSet rs = check.executeQuery();
    
                if (rs.next()) {
                    PreparedStatement update = conn.prepareStatement(
                        "UPDATE budgets SET amount = ? WHERE username = ? AND month = ?");
                    update.setDouble(1, budget);
                    update.setString(2, username);
                    update.setString(3, month);
                    update.executeUpdate();
                } else {
                    PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO budgets (username, month, amount) VALUES (?, ?, ?)");
                    insert.setString(1, username);
                    insert.setString(2, month);
                    insert.setDouble(3, budget);
                    insert.executeUpdate();
                }
    
                JOptionPane.showMessageDialog(panel,
                    "✅ Budget saved for " + month);
                loadBudgetForMonth(month,
                    budgetAmountField, usedLabel, remainingLabel, budgetBar);
    
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                    "❌ Please enter a valid number for budget.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel,
                    "💥 Database error: " + ex.getMessage());
            }
        });
    
        // Initial load
        loadBudgetForMonth(
            monthField.getText().trim(),
            budgetAmountField, usedLabel, remainingLabel, budgetBar
        );
    
        return panel;
    }
    
    // ==== 2) HELPER TO LOAD BUDGET STATUS ====
    private void loadBudgetForMonth(
            String month,
            JTextField budgetAmountField,
            JLabel usedLabel,
            JLabel remainingLabel,
            JProgressBar bar) {
        try {
            double budget = 0;
            double used   = getMonthlyExpense(month);
    
            // fetch stored budget
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT amount FROM budgets WHERE username = ? AND month = ?");
            stmt.setString(1, username);
            stmt.setString(2, month);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                budget = rs.getDouble("amount");
                budgetAmountField.setText(String.format("%.2f", budget));
            } else {
                budgetAmountField.setText("");
            }
    
            double remaining = budget - used;
            usedLabel.setText(String.format("₹%.2f", used));
            remainingLabel.setText(String.format("₹%.2f", remaining));
    
            int percent = (budget > 0)
                ? (int) Math.min((used / budget) * 100, 100)
                : 0;
            bar.setValue(percent);
    
            // Optional: immediate warning if already ≥90%
            if (percent >= 90) {
                JOptionPane.showMessageDialog(null,
                    "⚠️ You’re close to exceeding your budget!");
            }
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "⚠️ Could not load budget info: " + e.getMessage());
        }
    }
    
    // ==== 3) HELPER TO CALCULATE MONTHLY EXPENSES ====
    private double getMonthlyExpense(String month) {
        double total = 0;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT IFNULL(SUM(amount),0) FROM expenses " +
                "WHERE username = ? AND date LIKE ?");
            ps.setString(1, username);
            ps.setString(2, month + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) total = rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }  

    private double getCurrentBalance() {
        double income = 0, expense = 0;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT SUM(amount) FROM incomes WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) income = rs.getDouble(1);

            ps = conn.prepareStatement("SELECT SUM(amount) FROM expenses WHERE username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) expense = rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return income - expense;
    }

    private JPanel createAddExpensePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));
    
        // Top: current balance
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel balanceLabel = new JLabel("Current Balance: ₹" + getCurrentBalance());
        balanceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(balanceLabel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
    
        // Center: expense form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JTextField dateField     = new JTextField(LocalDate.now().toString());
        JTextField categoryField = new JTextField();
        JTextField amountField   = new JTextField();
        JTextField noteField     = new JTextField();
        JButton addBtn           = new JButton("Add Expense");
    
        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;                     formPanel.add(dateField, gbc);
    
        gbc.gridx = 0; gbc.gridy = ++y; formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;                     formPanel.add(categoryField, gbc);
    
        gbc.gridx = 0; gbc.gridy = ++y; formPanel.add(new JLabel("Amount (₹):"), gbc);
        gbc.gridx = 1;                     formPanel.add(amountField, gbc);
    
        gbc.gridx = 0; gbc.gridy = ++y; formPanel.add(new JLabel("Note:"), gbc);
        gbc.gridx = 1;                     formPanel.add(noteField, gbc);
    
        gbc.gridx = 1; gbc.gridy = ++y;    formPanel.add(addBtn, gbc);
    
        mainPanel.add(formPanel, BorderLayout.CENTER);
    
        // Tab‐through
        dateField.addActionListener(e -> categoryField.requestFocus());
        categoryField.addActionListener(e -> amountField.requestFocus());
        amountField.addActionListener(e -> noteField.requestFocus());
        noteField.addActionListener(e -> addBtn.doClick());
    
        // Add expense + budget‐alert logic
        addBtn.addActionListener(e -> {
            String date     = dateField.getText().trim();
            String category = categoryField.getText().trim();
            String note     = noteField.getText().trim();
            String amtText  = amountField.getText().trim();
    
            // Validation
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Please enter a valid date (YYYY-MM-DD).");
                dateField.requestFocus();
                return;
            }
            if (category.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Category is required.");
                categoryField.requestFocus();
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amtText);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Please enter a valid amount (> 0).");
                amountField.requestFocus();
                return;
            }
    
            // Insert into DB
            try {
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO expenses (username, date, category, amount, note) VALUES (?, ?, ?, ?, ?)"
                );
                stmt.setString(1, username);
                stmt.setString(2, date);
                stmt.setString(3, category);
                stmt.setDouble(4, amount);
                stmt.setString(5, note);
                stmt.executeUpdate();
    
                JOptionPane.showMessageDialog(mainPanel, "✅ Expense Added!");
    
                // Reset form
                categoryField.setText("");
                amountField.setText("");
                noteField.setText("");
                categoryField.requestFocus();
    
                // Update balance label
                balanceLabel.setText("Current Balance: ₹" + getCurrentBalance());
    
                // —— NEW: Budget Warning ——
                String month = date.substring(0, 7);  // e.g. "2025-04"
                double used  = getMonthlyExpense(month);
    
                // fetch your monthly budget
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT amount FROM budgets WHERE username = ? AND month = ?");
                ps.setString(1, username);
                ps.setString(2, month);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double budget = rs.getDouble("amount");
                    int pct = (int) ((used / budget) * 100);
                    if (pct >= 90) {
                        JOptionPane.showMessageDialog(mainPanel,
                            String.format(
                                "⚠️ You’ve used %d%% of your %s budget (₹%.2f/₹%.2f).",
                                pct, month, used, budget),
                            "Budget Warning",
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
    
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Database Error: " + ex.getMessage());
            }
        });
    
        return mainPanel;
    }
    
    
    

private JPanel createAddIncomePanel() {
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createTitledBorder("Add Income"));

    // Top Panel with Current Balance
    JPanel topPanel = new JPanel(new BorderLayout());
    JLabel balanceLabel = new JLabel("Current Balance: ₹" + getCurrentBalance());
    balanceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    topPanel.add(balanceLabel, BorderLayout.EAST);
    mainPanel.add(topPanel, BorderLayout.NORTH);

    // Form panel
    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JTextField dateField = new JTextField(java.time.LocalDate.now().toString());
    dateField.setToolTipText("Format: YYYY-MM-DD");

    JTextField sourceField = new JTextField();
    sourceField.setToolTipText("Example: Salary, Freelance, Gift");

    JTextField amountField = new JTextField();
    amountField.setToolTipText("Enter amount in ₹");

    JButton addBtn = new JButton("Add Income");

    int y = 0;

    gbc.gridx = 0; gbc.gridy = y;
    formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    formPanel.add(dateField, gbc);

    gbc.gridx = 0; gbc.gridy = ++y;
    formPanel.add(new JLabel("Source:"), gbc);
    gbc.gridx = 1;
    formPanel.add(sourceField, gbc);

    gbc.gridx = 0; gbc.gridy = ++y;
    formPanel.add(new JLabel("Amount (₹):"), gbc);
    gbc.gridx = 1;
    formPanel.add(amountField, gbc);

    gbc.gridx = 1; gbc.gridy = ++y;
    formPanel.add(addBtn, gbc);

    mainPanel.add(formPanel, BorderLayout.CENTER);

    // Focus Navigation
    dateField.addActionListener(e -> sourceField.requestFocus());
    sourceField.addActionListener(e -> amountField.requestFocus());
    amountField.addActionListener(e -> addBtn.doClick());

    // Add Income Logic
    addBtn.addActionListener(e -> {
        String date = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String amountText = amountField.getText().trim();

        if (date.isEmpty() || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter a valid date in YYYY-MM-DD format.");
            dateField.requestFocus();
            return;
        }

        if (source.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Source is required.");
            sourceField.requestFocus();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(mainPanel, "Amount must be greater than ₹0.");
                amountField.requestFocus();
                return;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(mainPanel, "Enter a valid numeric amount.");
            amountField.requestFocus();
            return;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO incomes (username, date, source, amount) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, username);
            stmt.setString(2, date);
            stmt.setString(3, source);
            stmt.setDouble(4, amount);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(mainPanel, "✅ Income Added!");

            // Reset Fields
            sourceField.setText("");
            amountField.setText("");
            sourceField.requestFocus();

            // Update Balance
            balanceLabel.setText("Current Balance: ₹" + getCurrentBalance());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Database Error: " + ex.getMessage());
        }
    });

    return mainPanel;
}

private JPanel createTransactionPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    String[] columnNames = {"Type", "Date", "Category/Source", "Amount", "Note"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    JTable table = new JTable(model);

    // Filter & Sort UI
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JTextField searchField = new JTextField(15);
    topPanel.add(new JLabel("🔍 Search:"));
    topPanel.add(searchField);

    String[] sortOptions = {
        "Date (Oldest First)", "Date (Newest First)",
        "Amount (Low to High)", "Amount (High to Low)"
    };
    JComboBox<String> sortCombo = new JComboBox<>(sortOptions);
    topPanel.add(new JLabel("↕ Sort By:"));
    topPanel.add(sortCombo);

    JButton refreshBtn = new JButton("🔄 Refresh");
    JButton downloadBtn = new JButton("⬇ Download Report"); // Download Button

    topPanel.add(refreshBtn);
    topPanel.add(downloadBtn); // Add download button to the panel
    panel.add(topPanel, BorderLayout.NORTH);
    panel.add(new JScrollPane(table), BorderLayout.CENTER);

    // Load Data Function
    Runnable loadData = () -> {
        model.setRowCount(0);
        String keyword = searchField.getText().trim().toLowerCase();
        String sortOrder = "";

        switch (sortCombo.getSelectedItem().toString()) {
            case "Date (Oldest First)": sortOrder = "date ASC"; break;
            case "Date (Newest First)": sortOrder = "date DESC"; break;
            case "Amount (Low to High)": sortOrder = "amount ASC"; break;
            case "Amount (High to Low)": sortOrder = "amount DESC"; break;
        }

        try {
            String sql = "SELECT 'Expense' AS type, date, category AS source, amount, note FROM expenses WHERE username = ? " +
                         "UNION ALL " +
                         "SELECT 'Income', date, source, amount, '' FROM incomes WHERE username = ? " +
                         "ORDER BY " + sortOrder;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                String date = rs.getString("date");
                String source = rs.getString("source");
                String note = rs.getString("note");
                double amount = rs.getDouble("amount");

                if (keyword.isEmpty() || type.toLowerCase().contains(keyword) ||
                    date.toLowerCase().contains(keyword) ||
                    source.toLowerCase().contains(keyword) ||
                    note.toLowerCase().contains(keyword) ||
                    String.valueOf(amount).contains(keyword)) {

                    model.addRow(new Object[]{type, date, source, amount, note});
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel, "Error loading transactions: " + ex.getMessage());
        }
    };

    // Download Report Function
    ActionListener downloadAction = e -> {
        try {
            String userHome = System.getProperty("user.home");
            Path downloadsPath = Path.of(userHome, "Downloads");
            String fileName = "transaction_report_" + System.currentTimeMillis() + ".csv";
            Path filePath = downloadsPath.resolve(fileName);

            try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(filePath))) {
                // Write headers
                for (int i = 0; i < model.getColumnCount(); i++) {
                    pw.print(model.getColumnName(i));
                    if (i != model.getColumnCount() - 1) pw.print(",");
                }
                pw.println();

                // Write data
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        pw.print(model.getValueAt(row, col));
                        if (col != model.getColumnCount() - 1) pw.print(",");
                    }
                    pw.println();
                }
            }

            JOptionPane.showMessageDialog(panel, "Report downloaded successfully!\nSaved to: " + filePath.toString());

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(panel, "Error downloading report: " + ex.getMessage());
        }
    };

    // Listeners
    refreshBtn.addActionListener(e -> loadData.run());
    downloadBtn.addActionListener(downloadAction);
    searchField.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { loadData.run(); }
        public void removeUpdate(DocumentEvent e) { loadData.run(); }
        public void changedUpdate(DocumentEvent e) { loadData.run(); }
    });
    sortCombo.addActionListener(e -> loadData.run());

    // Initial load
    loadData.run();

    return panel;
}

private JPanel createChartPanel() {
    JPanel chartContainer = new JPanel(new BorderLayout());
    JTabbedPane tabbedPane = new JTabbedPane();

    // Top panel with Refresh button
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton refreshButton = new JButton("🔄 Refresh Charts");
    topPanel.add(refreshButton);
    chartContainer.add(topPanel, BorderLayout.NORTH);

    // Method to load charts dynamically
    Runnable loadCharts = () -> {
        tabbedPane.removeAll(); // Clear old tabs before reloading

        // === Pie Chart: Expenses by Category ===
        try {
            DefaultPieDataset pieDataset = new DefaultPieDataset();
            String pieSQL = "SELECT UPPER(TRIM(category)) AS category, SUM(amount) AS total FROM expenses WHERE username=? GROUP BY UPPER(TRIM(category))";
            PreparedStatement pieStmt = conn.prepareStatement(pieSQL);
            pieStmt.setString(1, username);
            ResultSet pieRs = pieStmt.executeQuery();

            while (pieRs.next()) {
                pieDataset.setValue(pieRs.getString("category"), pieRs.getDouble("total"));
            }

            JFreeChart pieChart = ChartFactory.createPieChart3D(
                "Expenses by Category",
                pieDataset,
                true, true, false
            );

            PiePlot3D piePlot = (PiePlot3D) pieChart.getPlot();
            piePlot.setStartAngle(290);
            piePlot.setDirection(Rotation.CLOCKWISE);
            piePlot.setForegroundAlpha(0.6f);
            piePlot.setBackgroundPaint(Color.WHITE);
            piePlot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
            piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: ₹{1} ({2})"));
            piePlot.setOutlineVisible(false);

            pieChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
            pieChart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 13));

            ChartPanel pieChartPanel = new ChartPanel(pieChart);
            tabbedPane.addTab("📊 Category Pie Chart", pieChartPanel);

        } catch (Exception e) {
            tabbedPane.addTab("📊 Category Pie Chart", new JLabel("Error loading pie chart: " + e.getMessage()));
            e.printStackTrace();
        }

        // === Line Chart: Income vs Expense Monthly ===
        try {
            DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();

            String incomeSQL = "SELECT DATE_FORMAT(date, '%b %Y') AS month, SUM(amount) AS total FROM incomes WHERE username=? GROUP BY month ORDER BY MIN(date)";
            PreparedStatement incomeStmt = conn.prepareStatement(incomeSQL);
            incomeStmt.setString(1, username);
            ResultSet incomeRs = incomeStmt.executeQuery();
            while (incomeRs.next()) {
                String month = incomeRs.getString("month");
                double total = incomeRs.getDouble("total");
                lineDataset.addValue(total, "Income", month);
            }

            String expenseSQL = "SELECT DATE_FORMAT(date, '%b %Y') AS month, SUM(amount) AS total FROM expenses WHERE username=? GROUP BY month ORDER BY MIN(date)";
            PreparedStatement expenseStmt = conn.prepareStatement(expenseSQL);
            expenseStmt.setString(1, username);
            ResultSet expenseRs = expenseStmt.executeQuery();
            while (expenseRs.next()) {
                String month = expenseRs.getString("month");
                double total = expenseRs.getDouble("total");
                lineDataset.addValue(total, "Expense", month);
            }

            JFreeChart lineChart = ChartFactory.createLineChart(
                "Income vs Expense Over Time",
                "Month",
                "Amount (₹)",
                lineDataset,
                PlotOrientation.VERTICAL,
                true, true, false
            );

            CategoryPlot linePlot = lineChart.getCategoryPlot();
            linePlot.setBackgroundPaint(Color.WHITE);
            linePlot.setRangeGridlinePaint(Color.LIGHT_GRAY);

            LineAndShapeRenderer renderer = new LineAndShapeRenderer();
            renderer.setSeriesPaint(0, new Color(66, 135, 245)); // Income - Blue
            renderer.setSeriesPaint(1, new Color(255, 85, 85));   // Expense - Red
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            renderer.setSeriesStroke(1, new BasicStroke(2.0f));
            linePlot.setRenderer(renderer);

            lineChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
            lineChart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 13));

            ChartPanel lineChartPanel = new ChartPanel(lineChart);
            tabbedPane.addTab("📈 Income vs Expense", lineChartPanel);

        } catch (Exception e) {
            tabbedPane.addTab("📈 Income vs Expense", new JLabel("Error loading line chart: " + e.getMessage()));
            e.printStackTrace();
        }
    };

    // Attach action listener to refresh button
    refreshButton.addActionListener(e -> loadCharts.run());

    // First initial load
    loadCharts.run();

    // === Scrollable Container ===
    JScrollPane scrollPane = new JScrollPane(tabbedPane);
    chartContainer.add(scrollPane, BorderLayout.CENTER);

    return chartContainer;
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String loggedUser = JOptionPane.showInputDialog("Enter your username:");
            if (loggedUser != null && !loggedUser.trim().isEmpty()) {
                new ExpenseTrackerDashboard(loggedUser.trim());
            }
        });
    }
}

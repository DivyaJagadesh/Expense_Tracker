import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowLogin());
    }

    private static void createAndShowLogin() {
        // Create the main frame
        JFrame frame = new JFrame("Login - Expense Tracker");
        frame.setSize(500, 550);  // Increased height to accommodate back button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(240, 240, 250));  // Softer background

        // Main panel with rounded corners
        JPanel cardPanel = new RoundedPanel(30, new Color(255, 255, 255));
        cardPanel.setLayout(new BorderLayout(20, 20));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header with icon
        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        headerPanel.setOpaque(false);
        
        // Add back button at the top
        JButton btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBack.setForeground(new Color(100, 100, 100));
        btnBack.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Back button hover effect
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBack.setForeground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                btnBack.setForeground(new Color(100, 100, 100));
            }
        });

        headerPanel.add(btnBack);
        
        JLabel headerLabel = new JLabel("Welcome Back!", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        headerLabel.setForeground(new Color(44, 62, 80));

        JLabel subheaderLabel = new JLabel("Login to continue", JLabel.CENTER);
        subheaderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subheaderLabel.setForeground(new Color(127, 140, 141));

        headerPanel.add(headerLabel);
        headerPanel.add(subheaderLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field with icon
        JPanel usernamePanel = new JPanel(new BorderLayout(10, 0));
        usernamePanel.setOpaque(false);
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setForeground(new Color(52, 73, 94));
        
        JTextField txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);
        gbc.gridy = 1;
        formPanel.add(txtUsername, gbc);

        // Password field with icon
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setForeground(new Color(52, 73, 94));
        gbc.gridy = 2;
        formPanel.add(lblPassword, gbc);

        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridy = 3;
        formPanel.add(txtPassword, gbc);

        // Login Button with shadow effect
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
            BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));

        // Button hover effect
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(52, 152, 219));
            }
        });

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnLogin, gbc);

        // Footer with register option
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        footerPanel.setOpaque(false);

        JLabel footerLabel = new JLabel("Don't have an account?");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton btnRegister = new JButton("<HTML><U>Register here</U></HTML>");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegister.setForeground(new Color(52, 152, 219));
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Register button hover effect
        btnRegister.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnRegister.setForeground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                btnRegister.setForeground(new Color(52, 152, 219));
            }
        });

        footerPanel.add(footerLabel);
        footerPanel.add(btnRegister);

        // Add components to card panel
        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(footerPanel, BorderLayout.SOUTH);

        frame.add(cardPanel);
        frame.setVisible(true);

        // Button actions
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                showErrorDialog(frame, "Please enter both username and password.");
                return;
            }

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/exp_track",
                    "root",
                    "priya@2004"
                );

                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, 
                        "Login successful!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);

                    frame.dispose();
                    SwingUtilities.invokeLater(() -> {
                        new ExpenseTrackerDashboard(username);
                    });

                } else {
                    showErrorDialog(frame, "Invalid username or password.");
                }

                conn.close();

            } catch (SQLException ex) {
                showErrorDialog(frame, "Database connection error: " + ex.getMessage());
            }
        });

        btnRegister.addActionListener(e -> {
            frame.dispose();
            RegisterUser.main(new String[]{});
        });

        btnBack.addActionListener(e -> {
            frame.dispose();
            try {
                ExpenseTracker.main(new String[]{});
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private static void showErrorDialog(JFrame parent, String message) {
        JOptionPane.showMessageDialog(parent, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    // Custom panel for rounded corners with shadow effect
    static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;
        private ShadowType shadowType = ShadowType.CENTER;
        private int shadowSize = 6;
        private float shadowOpacity = 0.5f;
        private Color shadowColor = Color.BLACK;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            cornerRadius = radius;
            backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw shadow
            if (shadowType != ShadowType.NONE) {
                int x = shadowSize;
                int y = shadowSize;
                int width = getWidth() - shadowSize * 2;
                int height = getHeight() - shadowSize * 2;

                g2d.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), 
                                     shadowColor.getBlue(), (int)(255 * shadowOpacity)));
                
                for (int i = 0; i < shadowSize; i++) {
                    g2d.fillRoundRect(
                        x - shadowSize + i, 
                        y - shadowSize + i, 
                        width + shadowSize * 2 - i * 2, 
                        height + shadowSize * 2 - i * 2, 
                        cornerRadius, cornerRadius
                    );
                }
            }

            // Draw panel
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(
                shadowSize, 
                shadowSize, 
                getWidth() - shadowSize * 2, 
                getHeight() - shadowSize * 2, 
                cornerRadius, cornerRadius
            );

            g2d.dispose();
        }

        private enum ShadowType {
            CENTER, TOP, BOTTOM, LEFT, RIGHT, NONE
        }
    }
}
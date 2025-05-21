import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ExpenseTracker {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Main frame
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(240, 248, 255)); // Light, modern background

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50)); // More padding

        // Center panel for content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spacer
        centerPanel.add(Box.createVerticalGlue());

        // Title Label
        JLabel titleLabel = new JLabel("Track Your Finances Effortlessly");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Modern, bold font
        titleLabel.setForeground(new Color(65, 105, 225)); // Primary blue color
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0)); // Increased bottom padding
        centerPanel.add(titleLabel);

        // Subtitle Label
        JLabel subtitleLabel = new JLabel("Gain control over your spending and savings.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        subtitleLabel.setForeground(new Color(105, 105, 105)); // Darker grey
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        centerPanel.add(subtitleLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0)); // Increased button spacing
        buttonPanel.setOpaque(false);

        JButton loginButton = createStyledButton("Login", new Color(65, 105, 225)); // Primary blue
        JButton signupButton = createStyledButton("Sign Up", new Color(0, 128, 0));   // Green

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        centerPanel.add(buttonPanel);

        // Spacer
        centerPanel.add(Box.createVerticalGlue());

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);

        // Button Actions
        signupButton.addActionListener(e -> {
            frame.dispose();
            RegisterUser.main(new String[]{});
        });
        loginButton.addActionListener(e -> {
            frame.dispose();
            LoginPage.main(new String[]{});
        });
    }

    // Helper function to create styled buttons
    private static JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50)); // Increased padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            private Color originalColor = backgroundColor;
            private Color hoverColor = backgroundColor.darker();

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
        return button;
    }
}
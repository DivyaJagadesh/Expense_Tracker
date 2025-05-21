import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterUser {

    public static void main(String[] args) {
        // Create frame
        JFrame frame = new JFrame("Sign Up for Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 480); // Slightly taller for better spacing
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(230, 247, 255)); // Light blue background

        // Main panel with BorderLayout for overall structure
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(230, 247, 255));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30)); // More padding

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(230, 247, 255));
        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Arial", Font.BOLD, 26)); // More modern font
        title.setForeground(new Color(51, 51, 51)); // Dark grey
        headerPanel.add(title);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel with GridLayout for organized labels and fields
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 15, 20)); // Increased spacing
        formPanel.setBackground(new Color(230, 247, 255));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16); // Bold labels
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel lblUname = new JLabel("Full Name:");
        JTextField txtUname = new JTextField();

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();

        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();

        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        JPasswordField txtConfirmPassword = new JPasswordField();

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();

        JLabel lblPhone = new JLabel("Phone (Optional):"); // Indicate optional
        JTextField txtPhone = new JTextField();

        JButton btnSubmit = new JButton("Sign Up");

        // Apply consistent fonts and refined borders
        for (JLabel lbl : new JLabel[]{lblUname, lblUsername, lblPassword, lblConfirmPassword, lblEmail, lblPhone}) {
            lbl.setFont(labelFont);
            lbl.setForeground(new Color(77, 77, 77)); // Medium grey
        }
        for (JTextField txt : new JTextField[]{txtUname, txtUsername, txtEmail, txtPhone}) {
            txt.setFont(fieldFont);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(153, 204, 255)), // Light blue border
                    BorderFactory.createEmptyBorder(8, 10, 8, 10) // Padding inside
            ));
        }
        txtPassword.setFont(fieldFont);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(153, 204, 255)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtConfirmPassword.setFont(fieldFont);
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(153, 204, 255)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // Style the submit button with a more appealing look
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSubmit.setBackground(new Color(0, 123, 255)); // Stronger blue
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorder(BorderFactory.createRaisedBevelBorder()); // Subtle 3D effect

        // Add components to the form panel
        formPanel.add(lblUname);            formPanel.add(txtUname);
        formPanel.add(lblUsername);         formPanel.add(txtUsername);
        formPanel.add(lblPassword);         formPanel.add(txtPassword);
        formPanel.add(lblConfirmPassword);  formPanel.add(txtConfirmPassword);
        formPanel.add(lblEmail);            formPanel.add(txtEmail);
        formPanel.add(lblPhone);            formPanel.add(txtPhone);
        formPanel.add(new JLabel());        formPanel.add(btnSubmit); // Empty label for spacing

        mainPanel.add(formPanel, BorderLayout.CENTER);
        frame.add(mainPanel);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Submit button action listener
        btnSubmit.addActionListener(e -> {
            String uname = txtUname.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();

            if (uname.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Password Mismatch", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(frame, "Invalid email format. Please enter a valid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(frame, "Phone number must be 10 digits long.", "Invalid Phone Number", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/exp_track",
                    "root",
                    "priya@2004");
                 PreparedStatement pst = conn.prepareStatement(
                         "INSERT INTO users (uname, username, password, email, phone) VALUES (?, ?, ?, ?, ?)")) {

                pst.setString(1, uname);
                pst.setString(2, username);
                pst.setString(3, password);
                pst.setString(4, email);
                pst.setString(5, phone.isEmpty() ? null : phone); // Allow null for optional phone
                pst.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Account created successfully!", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close registration window
                SwingUtilities.invokeLater(() -> new ExpenseTrackerDashboard(username));
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(frame, "Username or Email already exists. Please choose a different one.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred during registration: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // For debugging purposes
            }
        });
    }
}
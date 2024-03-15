import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Login extends JFrame {
    private JTextField codeField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Login() {
        setTitle("Welcome to ACBS");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the background color of the JFrame
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loginPanel.setBackground(new Color(80, 80, 80)); // Set the background color of the panel

        JLabel codeLabel = new JLabel("Unique Code:");
        JLabel passwordLabel = new JLabel("Password");
        // Create custom fonts with increased size
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);

        codeLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);

        codeField = new JTextField();
        codeField.setForeground(Color.BLUE);
        passwordField = new JPasswordField();
        passwordField.setForeground(Color.BLUE);
        loginButton = new JButton("Login");
        loginButton.setForeground(Color.RED);
        registerButton = new JButton("Register");
        registerButton.setForeground(Color.RED);

        // Set font for text fields and buttons
        codeField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        loginButton.setFont(labelFont);
        registerButton.setFont(labelFont);

        loginPanel.add(codeLabel);
        loginPanel.add(codeField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel()); // Spacer
        loginPanel.add(loginButton);
        loginPanel.add(new JLabel()); // Spacer
        loginPanel.add(registerButton);

        add(loginPanel);

        passwordField.setEchoChar('*'); // Display password in "*"

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String code = codeField.getText();
                String password = new String(passwordField.getPassword());
                String result = isValidUser(code, password);

                if (result != null) {
                    if (result.equals("Customer")) {
                        JOptionPane.showMessageDialog(null, "Login Successful as " + result + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        openUsrDashboard(code);
                    } else if (result.equals("Admin")) {
                        openAdmDashboard();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                clearFormFields();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the SignupApp when the "Register" button is clicked
                new Register();
                dispose(); // Close the LoginApp
            }
        });

        setVisible(true);
    }

    private void openAdmDashboard() {
        // Redirect to the admin dashboard
        SwingUtilities.invokeLater(() -> {
            // Open the admin dashboard directly
            AdminPanel dashboard = new AdminPanel();
            dashboard.setVisible(true);
            this.hide();
            //dispose(); // Close the Login window
        });
    }

    private void openUsrDashboard(String code) {
        // Redirect to the user dashboard
        SwingUtilities.invokeLater(() -> {
            // Open the user dashboard directly
            UserPanel dashboard = new UserPanel(code);
            dashboard.setVisible(true);
            this.hide();
            //ispose(); // Close the Login window
        });
    }

    private String isValidUser(String code, String password) {
        if (code.equals("admin") && password.equals("admin")) {
            return "Admin";
        }

        String[] userTypes = {"Staff", "Customer"};
        for (String userType : userTypes) {
            String fileName = (String) userType.toLowerCase() + ".txt";
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] userInfo = line.split(":");
                    if (userInfo.length == 4 && userInfo[0].equals(code) && userInfo[1].equals(password)) {
                        return userType;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error reading file: " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null; // Invalid user
    }

    private void clearFormFields() {
        codeField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class Register extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField contactNumberField;
    private JButton signupButton;
    private JRadioButton customerRadioButton;

    public Register() {
        setTitle("Thank You For Joining Us!");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel signupPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        signupPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        signupPanel.setBackground(new Color(80, 80, 80)); // Set the background color of the panel

        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel contactNumberLabel = new JLabel("Contact Number:");
        JLabel userTypeLabel = new JLabel("User Type:");
        passwordField = new JPasswordField();
        passwordField.setForeground(Color.BLUE);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setForeground(Color.BLUE);
        emailField = new JTextField();
        emailField.setForeground(Color.BLUE);
        contactNumberField = new JTextField();
        contactNumberField.setForeground(Color.BLUE);
        signupButton = new JButton("Signup");
        signupButton.setForeground(Color.RED); // to color the button

        customerRadioButton = new JRadioButton("Customer");

        // Customize fonts and sizes
        Font labelFont = new Font("Roboto", Font.BOLD, 18);
        Font fieldFont = new Font("Roboto", Font.PLAIN, 18);
        passwordLabel.setFont(labelFont);
        confirmPasswordLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        contactNumberLabel.setFont(labelFont);
        userTypeLabel.setFont(labelFont);

        passwordField.setFont(fieldFont);
        confirmPasswordField.setFont(fieldFont);
        emailField.setFont(fieldFont);
        contactNumberField.setFont(fieldFont);
        signupButton.setFont(labelFont);
        customerRadioButton.setFont(labelFont);

        signupPanel.add(emailLabel);
        signupPanel.add(emailField);
        signupPanel.add(passwordLabel);
        signupPanel.add(passwordField);
        signupPanel.add(confirmPasswordLabel);
        signupPanel.add(confirmPasswordField);
        signupPanel.add(contactNumberLabel);
        signupPanel.add(contactNumberField);
        signupPanel.add(userTypeLabel);
        signupPanel.add(customerRadioButton);
        signupPanel.add(new JLabel()); // Spacer
        signupPanel.add(new JLabel()); // Spacer
        signupPanel.add(signupButton);

        add(signupPanel, BorderLayout.CENTER);

        passwordField.setEchoChar('*'); // Set the echo character to *
        confirmPasswordField.setEchoChar('*'); 
        // Create a "Sign In" button
        JButton signInButton = new JButton("Already have an account? Sign In");
        signInButton.setForeground(Color.RED);

        // Add an ActionListener to the "Sign In" button
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the Register window
                dispose();

                // Open the Login window
                new Login();
            }
        });

        // Add the "Sign In" button to the layout
        signupPanel.add(signInButton);

        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String email = emailField.getText();
                String contactNumber = contactNumberField.getText();

                // Check if any of the required fields is empty
                if (password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || contactNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop the signup process
                }

                if (password.length() < 5) {
                    JOptionPane.showMessageDialog(null, "Password should be at least 5 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop the signup process
                }

                if (password.equals(confirmPassword)) {
                    String uniqueCode = generateUniqueCode();
                    saveUserInfoToFile(uniqueCode, password, email, contactNumber);
                    JOptionPane.showMessageDialog(null, "Signup Successful! Your Unique Code: " + uniqueCode, "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Close the Register window
                    dispose();

                    // Open the Login window
                    new Login();
                } else {
                    JOptionPane.showMessageDialog(null, "Password and Confirm Password do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                clearFormFields();
            }
        });

        setVisible(true);
    }

    private String generateUniqueCode() {
        Random rand = new Random();
        int codeLength = 3;
        StringBuilder uniqueCode = new StringBuilder("COSR");

        for (int i = 0; i < codeLength; i++) {
            uniqueCode.append(rand.nextInt(10));
        }
        return uniqueCode.toString();
    }

    private void saveUserInfoToFile(String uniqueCode, String password, String email, String contactNumber) {
        String fileName = "customer.txt";

        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(uniqueCode + ":" + password + ":" + email + ":" + contactNumber + ":");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving user information.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFormFields() {
        passwordField.setText("");
        confirmPasswordField.setText("");
        emailField.setText("");
        contactNumberField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Register());
    }
}

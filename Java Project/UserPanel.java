import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class UserPanel extends JFrame {
    private JButton viewProfileButton;
    private JButton placeOrderButton;
    private JButton viewOrdersButton;
    private JButton logoutButton;
    private JButton feedbackButton;
    private JButton makePaymentButton; // Added Make Payment button
    private JTextArea profileTextArea;
    private String uniqueCode;
    private String userProfile;
    private menu menuWindow;

    public void setMenu(menu menuWindow) {
        this.menuWindow = menuWindow;
    }

    public UserPanel(String uniqueCode) {
        this.uniqueCode = uniqueCode;
        setTitle("APU Cafeteria");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel dashboardPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Increased rows to accommodate Make Payment button
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        viewProfileButton = new JButton("View Profile");
        placeOrderButton = new JButton("Place Order");
        viewOrdersButton = new JButton("View Orders");
        logoutButton = new JButton("Logout");
        feedbackButton = new JButton("Provide Feedback");
        makePaymentButton = new JButton("Make Payment"); // Added Make Payment button

        profileTextArea = new JTextArea();
        profileTextArea.setEditable(false);

        dashboardPanel.add(viewProfileButton);
        dashboardPanel.add(placeOrderButton);
        dashboardPanel.add(viewOrdersButton);
        dashboardPanel.add(feedbackButton);
        dashboardPanel.add(makePaymentButton); // Added Make Payment button
        dashboardPanel.add(logoutButton);

        viewProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewProfile();
            }
        });

        placeOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });

        viewOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewOrders();
            }
        });

        feedbackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provideFeedback();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        makePaymentButton.addActionListener(new ActionListener() { // Added action for Make Payment button
            public void actionPerformed(ActionEvent e) {
                openPaymentFrame();
            }
        });

        add(dashboardPanel, BorderLayout.NORTH);
        add(new JScrollPane(profileTextArea), BorderLayout.CENTER);
    }

    private void viewProfile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(":");
                if (userInfo.length == 4 && userInfo[0].equals(uniqueCode)) {
                    userProfile = "Unique Code: " + userInfo[0] + "\n";
                    userProfile += "Email: " + userInfo[2] + "\n";
                    userProfile += "Contact Number: " + userInfo[3] + "\n";
                    profileTextArea.setText(userProfile);
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void placeOrder() {
        // Customize this method to handle order placement logic
        menu mw = new menu();
        mw.setVisible(true);
       // menuWindow.setVisible(true);
    }

    private void viewOrders() {
        JFrame receiptFrame = new JFrame("View Orders");
        receiptFrame.setSize(400, 300);
        receiptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea receiptTextArea = new JTextArea();
        receiptTextArea.setEditable(false);

        try (BufferedReader reader = new BufferedReader(new FileReader("receipt1.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                receiptTextArea.append(line + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading orders.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        receiptFrame.add(scrollPane);
        receiptFrame.setVisible(true);
    }

    private void provideFeedback() {
        String feedback = JOptionPane.showInputDialog(this, "Enter your feedback:");
        if (feedback != null && !feedback.isEmpty()) {
            writeFeedbackToFile(feedback);
            JOptionPane.showMessageDialog(this, "Feedback submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void writeFeedbackToFile(String feedback) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("feedback.txt", true))) {
            writer.write(feedback);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error writing feedback to feedback.txt.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        this.setVisible(false);
        new Login().setVisible(true);
    }

    private void openPaymentFrame() { // Added method to open payment window
        JFrame paymentFrame = new JFrame("Make Payment");
        paymentFrame.setSize(300, 200);
        paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        paymentFrame.setLocationRelativeTo(null);

        JPanel paymentPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel billNumberLabel = new JLabel("Bill Number:");
        JTextField billNumberField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        JButton makePaymentConfirmButton = new JButton("Confirm Payment");

        makePaymentConfirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle payment confirmation logic here
                String billNumber = billNumberField.getText();
                String amount = amountField.getText();

                if (!billNumber.isEmpty() && !amount.isEmpty()) {
                    // Write payment details to pay.txt
                    writePaymentToFile(billNumber, amount);

                    // Optionally, perform further payment processing

                    JOptionPane.showMessageDialog(paymentFrame, "Payment was successfull.", "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    paymentFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(paymentFrame, "Please fill in both Bill Number and Amount.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        paymentPanel.add(billNumberLabel);
        paymentPanel.add(billNumberField);
        paymentPanel.add(amountLabel);
        paymentPanel.add(amountField);
        paymentPanel.add(new JLabel()); // Empty label for spacing
        paymentPanel.add(makePaymentConfirmButton);

        paymentFrame.add(paymentPanel);
        paymentFrame.setVisible(true);
    }

    private void writePaymentToFile(String billNumber, String amount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pay.txt", true))) {
            writer.write("Amount received " + amount + " from bill number " + billNumber);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error writing payment details to pay.txt.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String uniqueCode = readUniqueCodeFromFile("customer.txt");

            if (uniqueCode != null) {
                menu cafeMenu = new menu();
                UserPanel dashboard = new UserPanel(uniqueCode);
                dashboard.setMenu(cafeMenu);
                cafeMenu.setUserPanel(dashboard);
                dashboard.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Error: Unique code not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static String readUniqueCodeFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(":");
                if (userInfo.length == 4) {
                    return userInfo[0];
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading file: " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}

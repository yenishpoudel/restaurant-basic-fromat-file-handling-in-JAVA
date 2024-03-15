import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminPanel extends JFrame {
    private JTextArea receiptTextArea; // TextArea for displaying receipts

    public AdminPanel() {
        setTitle("Staffs of ACBS");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create receipt display area
        receiptTextArea = new JTextArea(20, 40);
        receiptTextArea.setEditable(false);
        JScrollPane receiptScrollPane = new JScrollPane(receiptTextArea);

        // Create a panel to hold menu and receipt displays
        JPanel displayPanel = new JPanel(new GridLayout(1, 2));
        displayPanel.add(receiptScrollPane);
        add(displayPanel, BorderLayout.CENTER);

        // Create buttons for admin actions
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton viewOrdersButton = new JButton("View Orders");
        JButton searchBillButton = new JButton("Search Bill");
        JButton salesReportButton = new JButton("Sales Report"); // Added "Sales Report" button
        JButton feedbackButton = new JButton("Feedback"); // Added "Feedback" button

        viewOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement logic to read and display orders from receipt.txt
                displayOrdersFromReceipt();
            }
        });

        searchBillButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show a dialog to enter the bill number
                String billNumber = JOptionPane.showInputDialog("Enter Bill Number:");
                if (billNumber != null && !billNumber.isEmpty()) {
                    // Search for the bill and display it
                    searchAndDisplayBill(billNumber);
                }
            }
        });

        salesReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the SalesReport window when the button is clicked
                SalesReportFrame salesReportFrame = new SalesReportFrame();
                salesReportFrame.setSize(800, 600); // Adjust the size as needed
                salesReportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                salesReportFrame.setVisible(true);
            }
        });

        feedbackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayFeedbackFromTxt(); // Call method to read and display feedback
            }
        });

        buttonPanel.add(viewOrdersButton);
        buttonPanel.add(searchBillButton);
        buttonPanel.add(salesReportButton);
        buttonPanel.add(feedbackButton); // Add the "Feedback" button

        // Create a "Logout" button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the current Admin Panel window
                dispose();

                // Open the Login page
                Login login = new Login();
                login.setVisible(true);
            }
        });

        buttonPanel.add(logoutButton); // Add the "Logout" button

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void displayOrdersFromReceipt() {
        // Read orders from receipt.txt and display them in the receiptTextArea
        StringBuilder receiptText = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("receipt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                receiptText.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiptTextArea.setText(receiptText.toString());
    }

    private void searchAndDisplayBill(String billNumber) {
        // Search for a bill by its number in receipt.txt and display it
        StringBuilder billText = new StringBuilder();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("receipt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Bill #" + billNumber)) {
                    found = true;
                }
                if (found) {
                    billText.append(line).append("\n");
                    if (line.startsWith("Total:")) {
                        break; // Reached the end of the bill
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (found) {
            // Display the bill in a new window
            JFrame billFrame = new JFrame("Bill Details");
            JTextArea billTextArea = new JTextArea(20, 40);
            billTextArea.setText(billText.toString());
            billTextArea.setEditable(false);

            JScrollPane billScrollPane = new JScrollPane(billTextArea);
            billFrame.add(billScrollPane);
            billFrame.setSize(400, 400);
            billFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Bill not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayFeedbackFromTxt() {
        // Read feedback from feedback.txt and display it
        StringBuilder feedbackText = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                feedbackText.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTextArea feedbackTextArea = new JTextArea(20, 40);
        feedbackTextArea.setText(feedbackText.toString());
        feedbackTextArea.setEditable(false);

        JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea);
        JFrame feedbackFrame = new JFrame("Feedback");
        feedbackFrame.add(feedbackScrollPane);
        feedbackFrame.setSize(400, 400);
        feedbackFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminPanel();
        });
    }
}

class SalesReportFrame extends JFrame {
    private JTextArea salesReportTextArea;

    public SalesReportFrame() {
        setTitle("Sales Report");
        setLayout(new BorderLayout());

        salesReportTextArea = new JTextArea(20, 40);
        salesReportTextArea.setEditable(false);
        JScrollPane salesReportScrollPane = new JScrollPane(salesReportTextArea);

        add(salesReportScrollPane, BorderLayout.CENTER);

        // Generate the sales report from receipt.txt
        generateSalesReport();

        pack(); // Automatically set the frame size based on the content
    }

    private void generateSalesReport() {
        StringBuilder salesReportText = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("receipt.txt"))) {
            String line;
            double totalSales = 0.0;

            salesReportText.append("Sales Report\n\n");
            salesReportText.append(String.format("%-20s%-10s%-12s%n", "Item", "Price", "Quantity"));

            while ((line = br.readLine()) != null) {
                if (line.matches(".* - \\$[0-9]+\\.[0-9]+ \\(Quantity: [0-9]+\\)")) {
                    // Extract item, price, and quantity
                    String[] parts = line.split(" - ");
                    String item = parts[0].trim();
                    String pricePart = parts[1].trim();
                    String priceStr = pricePart.substring(pricePart.indexOf("$") + 1, pricePart.indexOf(" (Quantity:"));
                    double price = Double.parseDouble(priceStr);
                    String quantityPart = pricePart.substring(pricePart.indexOf("Quantity: ") + 10, pricePart.length() - 1);
                    int quantity = Integer.parseInt(quantityPart);

                    // Append the item details to the sales report
                    salesReportText.append(String.format("%-20s$%-10.2f%-12d%n", item, price, quantity));

                    // Update the total sales
                    totalSales += price * quantity;
                }
            }

            // Append total sales to the report
            salesReportText.append("\nTotal Sales: $" + totalSales);
        } catch (IOException e) {
            e.printStackTrace();
        }
        salesReportTextArea.setText(salesReportText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SalesReportFrame salesReportFrame = new SalesReportFrame();
            salesReportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            salesReportFrame.setVisible(true);
        });
    }
}
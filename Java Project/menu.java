import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class menu extends JFrame implements ActionListener {
    private ArrayList<JPanel> menuItemPanels;
    private ArrayList<JCheckBox> menuItems;
    private ArrayList<JSpinner> quantitySpinners; // Added quantity spinners
    private JButton placeOrderButton;
    private JButton clearButton;
    private JButton goBackButton; // Added Go Back button
    private JLabel totalLabel;

    private double totalBill = 0.0;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private ArrayList<Integer> selectedQuantities = new ArrayList<>();
    private UserPanel usrDashboard; // Reference to the UsrDashboard

    public void setUsrDashboard(UserPanel usrDashboard) {
        this.usrDashboard = usrDashboard;
    }

    public menu() {
        setTitle("Cafe Menu");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the window, not exit application
        setLayout(new GridLayout(10, 1));

        menuItemPanels = new ArrayList<>();
        menuItems = new ArrayList<>();
        quantitySpinners = new ArrayList<>();

        // Add menu items with quantity spinners
        for (String menuItemText : getMenuItemsList()) {
            JPanel menuItemPanel = new JPanel(new BorderLayout());
            JCheckBox menuItemCheckBox = new JCheckBox(menuItemText);
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

            menuItemPanel.add(menuItemCheckBox, BorderLayout.WEST);
            menuItemPanel.add(quantitySpinner, BorderLayout.EAST);

            menuItemPanels.add(menuItemPanel);
            menuItems.add(menuItemCheckBox);
            quantitySpinners.add(quantitySpinner);
        }

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(this);

        clearButton = new JButton("Clear Menu");
        clearButton.addActionListener(this);

        goBackButton = new JButton("Go Back"); // Create a Go Back button
        goBackButton.addActionListener(this);
        goBackButton.setVisible(true);

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Add menu item panels
        for (JPanel menuItemPanel : menuItemPanels) {
            add(menuItemPanel);
        }
        add(placeOrderButton);
        add(clearButton);
        add(goBackButton); // Add the Go Back button to the frame
        add(totalLabel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == placeOrderButton) {
            totalBill = 0.0;
            selectedItems.clear();
            selectedQuantities.clear();

            for (int i = 0; i < menuItems.size(); i++) {
                JCheckBox item = menuItems.get(i);
                JSpinner quantitySpinner = quantitySpinners.get(i);

                if (item.isSelected()) {
                    String[] parts = item.getText().split(" - ");
                    if (parts.length == 2) {
                        double price = Double.parseDouble(parts[1].substring(1));
                        int quantity = (int) quantitySpinner.getValue();
                        double itemTotal = price * quantity;

                        totalBill += itemTotal;

                        // Add the selected item and quantity to the lists
                        selectedItems.add(item.getText() + " (Quantity: " + quantity + ")");
                        selectedQuantities.add(quantity);
                    }
                }
            }

            totalLabel.setText("Total: $" + String.format("%.2f", totalBill));

            // Save the order to receipt.txt with a unique bill number
            saveOrderToReceipt();

            // Show order confirmation
            showOrderConfirmation();
        } else if (e.getSource() == clearButton) {
            // Clear menu selection and total
            clearMenuSelection();
        } else if (e.getSource() == goBackButton) {
            // Close the menu window when "Go Back" is clicked
            dispose(); // Dispose of the menu window
        }
    }

    private void saveOrderToReceipt() {
        try {
            String receiptFileName = "receipt.txt";
            String receipt1FileName = "receipt1.txt";
            String billNumber = generateBillNumber();
            FileWriter receiptWriter = new FileWriter(receiptFileName, true);
            FileWriter receipt1Writer = new FileWriter(receipt1FileName, false); // Renew receipt1.txt

            receiptWriter.write("\nBill #" + billNumber + " - Date: " + getCurrentDateTime() + "\n");
            receipt1Writer.write("\nBill #" + billNumber + " - Date: " + getCurrentDateTime() + "\n");

            for (int i = 0; i < selectedItems.size(); i++) {
                String selectedItem = selectedItems.get(i);
                int quantity = selectedQuantities.get(i);

                receiptWriter.write(selectedItem + "\n");
                receipt1Writer.write(selectedItem + "\n");

                // Update quantity in receipt1.txt
                String updatedItem = selectedItem.replace("(Quantity: 0)", "(Quantity: " + quantity + ")");
                receipt1Writer.write(updatedItem + "\n");
            }

            receiptWriter.write("Total: $" + String.format("%.2f", totalBill) + "\n\n");
            receipt1Writer.write("Total: $" + String.format("%.2f", totalBill) + "\n\n");

            receiptWriter.close();
            receipt1Writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String generateBillNumber() {
        // Generate a random bill number between 10000 and 99999
        return String.valueOf((int) (Math.random() * 90000) + 10000);
    }

    private void clearMenuSelection() {
        for (int i = 0; i < menuItems.size(); i++) {
            JCheckBox item = menuItems.get(i);
            item.setSelected(false);
            JSpinner quantitySpinner = quantitySpinners.get(i);
            quantitySpinner.setValue(0);
        }
        totalBill = 0.0;
        totalLabel.setText("Total: $0.00");
        selectedItems.clear();
        selectedQuantities.clear();
    }

    private void showOrderConfirmation() {
        JFrame confirmationFrame = new JFrame("Order Confirmed");
        confirmationFrame.setSize(300, 100);
        confirmationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel confirmationLabel = new JLabel("Order Confirmed!");
        confirmationLabel.setHorizontalAlignment(JLabel.CENTER);

        confirmationFrame.add(confirmationLabel);
        confirmationFrame.setVisible(true);
    }

    private String[] getMenuItemsList() {
        return new String[]{
                "Pizza - $10.99",
                "Burger - $5.99",
                "Coffee - $2.49",
                "Tea - $1.99",
                "Sandwich - $7.99",
                "Cheese Cake - $6.49",
                "Brown Bread - $3.99",
                "Cinnamon Roll - $4.49",
                "Croissant - $3.99",
                "Danish Roll - $4.99",
                "Muffins - $2.99",
                "Pastries - $3.49",
                "Cake - $8.99",
                "Bubble Tea - $4.99"
        };
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            menu cafeMenu = new menu();
            cafeMenu.setVisible(true);
        });
    }

    public String getOrderDetails() {
        return null;
    }

    public void setUserPanel(UserPanel dashboard) {
    }
}

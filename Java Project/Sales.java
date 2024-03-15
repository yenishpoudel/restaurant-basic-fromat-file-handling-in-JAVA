import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class Sales {
    public static void generateSalesReport() {
        // Create a StringBuilder to store the sales report content
        StringBuilder salesReportText = new StringBuilder();

        // Read orders from receipt.txt and calculate total sales
        double totalSales = 0.0;
        try (BufferedReader br = new BufferedReader(new FileReader("receipt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Check if the line contains a sales item (e.g., "Tea - $1.99 (Quantity: 5)")
                if (line.matches(".* - \\$[0-9]+\\.[0-9]+ \\(Quantity: [0-9]+\\)")) {
                    salesReportText.append(line).append("\n");
                    // Extract the price from the line and add it to the total sales
                    String[] parts = line.split(" - ");
                    String pricePart = parts[1].trim();
                    String priceStr = pricePart.substring(pricePart.indexOf("$") + 1, pricePart.indexOf(" (Quantity:"));
                    double price = Double.parseDouble(priceStr);
                    totalSales += price;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Append the total sales to the sales report
        salesReportText.append("\nTotal Sales: $").append(totalSales);

        // Create and display a window to show the sales report
        JFrame salesReportFrame = new JFrame("Sales Report");
        JTextArea salesReportTextArea = new JTextArea(20, 40);
        salesReportTextArea.setText(salesReportText.toString());
        salesReportTextArea.setEditable(false);

        JScrollPane salesReportScrollPane = new JScrollPane(salesReportTextArea);
        salesReportFrame.add(salesReportScrollPane);
        salesReportFrame.setSize(400, 400);
        salesReportFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            generateSalesReport();
        });
    }

    public void setVisible(boolean b) {
    }

    public void displaysalesreport() {
    }

    public void displaySales() {
    }

    public String generateReport() {
        return null;
    }

    public void setSize(int i, int j) {
    }

    public void setDefaultCloseOperation(int disposeOnClose) {
    }
}

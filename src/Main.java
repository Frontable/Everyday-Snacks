

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Read products and clients from Excel file
        Product[] products = readProductsFromExcel("products.xlsx");
        Client[] clients = readClientsFromExcel("clients.xlsx");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter order details (Client ID=Quantity, separated by commas):");
        String input = scanner.nextLine();
        String[] orderDetails = input.split(",");

        int clientId = Integer.parseInt(orderDetails[0]);

        Order order = new Order(clients[clientId - 1]);

        for (int i = 1; i < orderDetails.length; i++) {
            String[] itemDetails = orderDetails[i].split("=");
            int productId = Integer.parseInt(itemDetails[0]);
            int quantity = Integer.parseInt(itemDetails[1]);
            order.addItem(new OrderItem(products[productId - 1], quantity));
        }

        printOrderSummary(order);
    }

    private static Product[] readProductsFromExcel(String filename) {
        try (FileInputStream fis = new FileInputStream(new File(filename));
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming products are in the first sheet
            int rowCount = sheet.getPhysicalNumberOfRows();
            Product[] products = new Product[rowCount - 1]; // Exclude header row

            for (int i = 1; i < rowCount; i++) { // Skip header row
                Row row = sheet.getRow(i);
                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                double unitCost = row.getCell(2).getCellType() == CellType.NUMERIC ? row.getCell(2).getNumericCellValue() : 0.0;
                String markup = row.getCell(3).getStringCellValue();
                String promoDescription = row.getCell(4).getStringCellValue();
                products[i - 1] = new Product(id, name, unitCost, markup, promoDescription);
            }

            return products;
        } catch (IOException e) {
            e.printStackTrace();
            return new Product[0];
        }
    }

    private static Client[] readClientsFromExcel(String filename) {
        try (FileInputStream fis = new FileInputStream(new File(filename));
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming clients are in the first sheet
            int rowCount = sheet.getPhysicalNumberOfRows();
            Client[] clients = new Client[rowCount - 1]; // Exclude header row

            for (int i = 1; i < rowCount; i++) { // Skip header row
                Row row = sheet.getRow(i);
                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                double basicDiscount = row.getCell(2).getNumericCellValue();
                double additionalDiscountBelow10k = row.getCell(3).getNumericCellValue();
                double additionalDiscountAbove10k = row.getCell(4).getNumericCellValue();
                clients[i - 1] = new Client(id, name, basicDiscount, additionalDiscountBelow10k, additionalDiscountAbove10k);
            }

            return clients;
        } catch (IOException e) {
            e.printStackTrace();
            return new Client[0];
        }
    }

    private static void printOrderSummary(Order order) {
        order.calculatePrices(); // Calculate prices based on client discounts

        Client client = order.getClient();
        System.out.println("Client: " + client.getName());
        System.out.println("Product" + " ".repeat(16) +"Quantity" + " ".repeat(7) +
                "Standard Unit Price" + " ".repeat(3) + "Promotional Unit Price" + " ".repeat(11) + "Line Total");


        double totalBeforeDiscount = 0.0;

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantityOrdered();
            double standardUnitPrice = item.getStandardUnitPrice();
            double promoUnitPrice = item.getPromoUnitPrice();

            double lineTotal = promoUnitPrice * quantity;
            totalBeforeDiscount += lineTotal;

            System.out.printf("%-20s %,10d %,25.2f", product.getName(), quantity, standardUnitPrice);
            if (promoUnitPrice != standardUnitPrice) {
                System.out.printf("%,25.5f", promoUnitPrice);
            } else {
                System.out.printf("%25s", ""); // Print empty space if no promotional price
            }
            System.out.printf("%,21.2f%n", lineTotal);
        }

        System.out.println("Total Before Client Discounts:" + " ".repeat(7) + "EUR " + String.format("%,.2f", totalBeforeDiscount));

        double basicDiscount = totalBeforeDiscount * client.getBasicDiscount();
        double additionalDiscount = totalBeforeDiscount * client.getAdditionalDiscount(totalBeforeDiscount);

        if (additionalDiscount > 0) {
            System.out.printf("Additional Volume Discount at %.0f%%:" + " ".repeat(4)
                    + "EUR %,.2f%n", client.getAdditionalDiscount(totalBeforeDiscount) * 100, additionalDiscount);
        }

        double orderTotal = totalBeforeDiscount - basicDiscount - additionalDiscount;
        System.out.println("Order Total Amount:" + " ".repeat(18) + "EUR " + String.format("%,.3f", orderTotal).replace(".", ","));
    }
}

import java.util.Scanner;

public class Main {
    private static final Product[] products = {
            new Product(1, "Danish Muffin", 0.52, "80%", "none"),
            new Product(2, "Granny’s Cup Cake", 0.38, "120%", "30% off"),
            new Product(3, "Frenchy’s Croissant", 0.41, "0.90 EUR/unit", "0.90 EUR/unit"),
            new Product(4, "Crispy chips", 0.60, "1.00 EUR/unit", "Buy 2, get 3rd free")
    };

    private static final Client[] clients = {
            new Client(1, "ABC Distribution", 0.05, 0.00, 0.02),
            new Client(2, "DEF Foods", 0.04, 0.01, 0.02),
            new Client(3, "GHI Trade", 0.03, 0.01, 0.03),
            new Client(4, "JKL Kiosks", 0.02, 0.03, 0.05),
            new Client(5, "MNO Vending", 0.00, 0.05, 0.07)
    };

    public static void main(String[] args) {
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

import java.util.ArrayList;
import java.util.List;

public class Order {
    private Client client;
    private List<OrderItem> items;

    public Order(Client client) {
        this.client = client;
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public Client getClient() {
        return client;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void calculatePrices() {
        for (OrderItem item : items) {
            Product product = item.getProduct();
            int quantity = item.getQuantityOrdered();

            double standardUnitPrice = product.calculateStandardUnitPrice(quantity);
            double promoUnitPrice = product.calculatePrice(quantity);

            item.setStandardUnitPrice(standardUnitPrice);
            item.setPromoUnitPrice(promoUnitPrice);
        }
    }
}

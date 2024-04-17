public class OrderItem {
    private Product product;
    private int quantityOrdered;
    private double standardUnitPrice;
    private double promoUnitPrice;

    public OrderItem(Product product, int quantityOrdered) {
        this.product = product;
        this.quantityOrdered = quantityOrdered;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public double getStandardUnitPrice() {
        return standardUnitPrice;
    }

    public void setStandardUnitPrice(double standardUnitPrice) {
        this.standardUnitPrice = standardUnitPrice;
    }

    public double getPromoUnitPrice() {
        return promoUnitPrice;
    }

    public void setPromoUnitPrice(double promoUnitPrice) {
        this.promoUnitPrice = promoUnitPrice;
    }
}

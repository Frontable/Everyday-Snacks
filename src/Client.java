public class Client {
    private int id;
    private String name;
    private double basicDiscountPercentage;
    private double additionalDiscountAbove10kPercentage;
    private double additionalDiscountAbove30kPercentage;

    public Client(int id, String name, String basicDiscountPercentage, String additionalDiscountAbove10kPercentage, String additionalDiscountAbove30kPercentage) {
        this.id = id;
        this.name = name;
        this.basicDiscountPercentage = parsePercentage(basicDiscountPercentage);
        this.additionalDiscountAbove10kPercentage = parsePercentage(additionalDiscountAbove10kPercentage);
        this.additionalDiscountAbove30kPercentage = parsePercentage(additionalDiscountAbove30kPercentage);
    }

    private double parsePercentage(String percentage) {
        if (percentage.endsWith("%")) {
            percentage = percentage.substring(0, percentage.length() - 1); // Remove "%" sign
            return Double.parseDouble(percentage) / 100; // Transform percentage to fraction
        } else {
            throw new IllegalArgumentException("Invalid percentage format: " + percentage);
        }
    }

    public String getName() {
        return name;
    }

    public double getBasicDiscount() {
        return basicDiscountPercentage;
    }

    public double getAdditionalDiscount(double orderTotal) {
        if (orderTotal < 10000) {
            return 0;
        } else if (orderTotal < 30000) {
            return additionalDiscountAbove10kPercentage;
        } else {
            return additionalDiscountAbove30kPercentage;
        }
    }
}

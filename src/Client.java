public class Client {
    private int id;
    private String name;
    private double basicDiscount;
    private double additionalDiscountAbove10k;
    private double additionalDiscountAbove30k;

    public Client(int id, String name, double basicDiscount, double additionalDiscountAbove10k, double additionalDiscountAbove30k) {
        this.id = id;
        this.name = name;
        this.basicDiscount = basicDiscount;
        this.additionalDiscountAbove10k = additionalDiscountAbove10k;
        this.additionalDiscountAbove30k = additionalDiscountAbove30k;
    }

    public String getName() {
        return name;
    }

    public double getBasicDiscount() {
        return basicDiscount;
    }

    public double getAdditionalDiscount(double orderTotal) {
        if (orderTotal < 10000) {
            return 0;
        } else if (orderTotal < 30000) {
            return additionalDiscountAbove10k;
        } else {
            return additionalDiscountAbove30k;
        }
    }
}

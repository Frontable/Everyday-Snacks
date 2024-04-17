import java.math.RoundingMode;

public class Product {
    private int id;
    private String name;
    private double unitCost;
    private String markup;
    private String promoDescription;

    public Product(int id, String name, double unitCost, String markup, String promoDescription) {
        this.id = id;
        this.name = name;
        this.unitCost = unitCost;
        this.markup = markup;
        this.promoDescription = promoDescription;
    }

    public double calculateStandardUnitPrice(int quantity){
        double price = unitCost;
        double discount;
        if (markup.endsWith("%")) {
            // Percentage case
            double percentage = Double.parseDouble(markup.substring(0, markup.length() - 1));
            double pricePercentage = (price * percentage) / 100;
            price = price + pricePercentage;

        } else {
            // Unit price case
            double unitPrice = Double.parseDouble(markup.split(" ")[0]);
            price = price + unitPrice;
        }
        return price;
    }

    public double calculatePrice(int quantity) {
        double price = unitCost;
        double discount;
        if (markup.endsWith("%")) {
            // Percentage case
            double percentage = Double.parseDouble(markup.substring(0, markup.length() - 1));
            price =  price * (1 + percentage / 100);

        } else {
            // Unit price case
            double unitPrice = Double.parseDouble(markup.split(" ")[0]);
            price = price + unitPrice;
        }

        if(promoDescription == "30% off"){
            discount = price * 0.30;
            price = price - discount;
        }

        if(promoDescription == "Buy 2, get 3rd free"){
            //4.80, 1.60, 33,33%
            discount = price * 0.3333;
            price = price - discount;
        }
        return price;
    }

    public String getName() {
        return name;
    }

    public String getPromoDescription() {
        return promoDescription;
    }
}

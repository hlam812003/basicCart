package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;

public class Cart implements Serializable {
    private ArrayList<LineItem> items;

    private double discount = 0;

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;

        for (LineItem lineItem : items) {
            lineItem.setDiscount(discount);
        }
    }

    public Cart() {
        items = new ArrayList<LineItem>();
    }

    public ArrayList<LineItem> getItems() {
        return items;
    }

    public int getCount() {
        return items.size();
    }

    public void addItem(LineItem item) {
        // Check if the product in the line item is not null
        if (item != null && item.getProduct() != null) {
            String code = item.getProduct().getCode();
            int quantity = item.getQuantity();

            // if contain product in cart --> increase quantity
            for (LineItem lineItem : items) {
                if (lineItem.getProduct().getCode().equals(code)) {
                    lineItem.setQuantity(lineItem.getQuantity() + quantity);
                    return;
                }
            }
            // else add new line item to cart
            item.setDiscount(this.discount);
            items.add(item);
        } else {
            // Handle the case where the product is null
            System.err.println("LineItem or Product is null and cannot be added to the cart.");
        }
    }

    public void removeItem(LineItem item) {
        String code = item.getProduct().getCode();
        for (int i = 0; i < items.size(); i++) {
            LineItem lineItem = items.get(i);
            if (lineItem.getProduct().getCode().equals(code)) {
                items.remove(i);
                return;
            }
        }
    }

    public void updateItem(LineItem item) {
        String code = item.getProduct().getCode();
        int quantity = item.getQuantity();
        for (int i = 0; i < items.size(); i++) {
            LineItem lineItem = items.get(i);
            if (lineItem.getProduct().getCode().equals(code)) {
                lineItem.setQuantity(quantity);
                return;
            }
        }
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (LineItem item : items) {
            total += item.getTotal();
        }
        return total;
    }

    public String getTotalCurrencyFormat() {
        double total = getTotalAmount();
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(total);
    }

    // private double validateCouponCode(String couponCode) {
    // if ("SAVE10".equals(couponCode)) {
    // return 10.0;
    // } else {
    // return 0.0;
    // }
    // }

    public void applyDiscount(double discountPercentage) {
        // double currentTotal = this.getTotalAmount(); // Get the current total
        // double discountAmount = currentTotal * discountPercentage / 100.0;
        // double newTotal = currentTotal - discountAmount;

        // // Log to check
        // System.out.println("Applying discount: " + discountPercentage + "%");
        // System.out.println("Discount amount: " + discountAmount);
        // System.out.println("New total: " + newTotal);

        // if (newTotal < 0) {
        // newTotal = 0; // Ensure total doesn't become negative
        // }

        for (LineItem lineItem : items) {
            lineItem.setDiscount(discountPercentage);
        }
    }

}
package output;

import item.Items;

public class ItemDisplay {
    public static void display(
            Items item,
            double[] price,
            double totalPrice) {

        double tax = price[0];
        double finalPrice = price[1];

        System.out.println("\n----- Item Details -----");
        System.out.println("Item Name: " + item.getName());
        System.out.println("Item Type: " + item.getType());
        System.out.println("Item Price: " + item.getPrice());
        System.out.println("Quantity: " + item.getQuantity());
        System.out.println("Total Price: " + totalPrice);
        System.out.println("Tax: " + tax);
        System.out.println("Final Price: " + finalPrice);
        System.out.println("------------------------\n");
    }
}
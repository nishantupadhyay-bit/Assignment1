import enums.Type;
import input.ItemInput;
import item.Items;
import output.ItemDisplay;
import tax.CalculateTaxOfItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Items> items = new ArrayList<>();
        Map<String, ArrayList<Type>> itemMapWithType = new HashMap<>();

        while (true) {
            System.out.print("Do you want to enter details of any item (y/n): ");
            String response = sc.next().toLowerCase();

            if (response.equals("n") || response.equals("no")) {
                break;
            }

            if (!response.equals("y") && !response.equals("yes")) {
                System.out.println("Invalid response. Please enter y or n.");
                continue;
            }

            try {
                Items item = ItemInput.readItem(sc, itemMapWithType);
                items.add(item);
                itemMapWithType.computeIfAbsent(item.getName(), k -> new ArrayList<>()).add(item.getType());
                System.out.println("Item details accepted successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        for (Items item : items) {
            double[] prices = CalculateTaxOfItem.calculateTaxOfItem(item);
            double totalPrice = item.getPrice() * item.getQuantity();
            ItemDisplay.display(item, prices, totalPrice);
        }

        sc.close();
    }
}
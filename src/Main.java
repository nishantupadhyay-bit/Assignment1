import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

enum Type {
    Raw,
    Manufactured,
    Imported
}

class Items {
    String name;
    double price;
    int quantity;
    Type type;
}

class ValidateItem {

    // Validates all item details
    public static void validateItem(Items item, HashMap<String, ArrayList<Type>> itemMapWithType) {
        validateName(item.name);
        validatePrice(item.price);
        validateQuantity(item.quantity);
        validateType(item.type);
        validateDuplicacy(itemMapWithType, item.name, item.type);
    }

    // Validates item name
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
    }

    // Validates item price
    public static void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }

    // Validates item quantity
    public static void validateQuantity(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    // Checks for duplicate items
    public static void validateDuplicacy(HashMap<String, ArrayList<Type>> itemMapWithType, String name, Type type) {
        if (itemMapWithType.containsKey(name) && itemMapWithType.get(name).contains(type)) {
            throw new IllegalArgumentException("Same item already exists in DB");
        }
    }

    // Validates item type
    public static void validateType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Item type cannot be null");
        }
    }
}

interface TaxCalculation {
    double getTaxCalculate(double totalPrice);
}

class RawTaxCalculation implements TaxCalculation {

    @Override
    public double getTaxCalculate(double totalPrice) {
        // Raw item tax is 12.5%
        return totalPrice * 0.125;
    }
}

class ManufactureTaxCalculation implements TaxCalculation {

    @Override
    public double getTaxCalculate(double totalPrice) {
        // Calculate basic tax
        double basicTax = totalPrice * 0.125;

        // Calculate additional tax
        double additionalTax = (totalPrice + basicTax) * 0.02;

        return basicTax + additionalTax;
    }
}

class ImportedTaxCalculation implements TaxCalculation {

    @Override
    public double getTaxCalculate(double totalPrice) {
        // Calculate import duty
        double importDuty = totalPrice * 0.10;
        double finalCost = totalPrice + importDuty;
        double surcharge;

        // Calculate surcharge based on final cost
        if (finalCost <= 100) {
            surcharge = 5;
        } else if (finalCost <= 200) {
            surcharge = 10;
        } else {
            surcharge = finalCost * 0.05;
        }

        return importDuty + surcharge;
    }
}

class CalculateTaxOfItem {

    public static double[] calculateTaxOfItem(Items item) {
        TaxCalculation taxCalculation;

        // Select tax calculation based on item type
        switch (item.type) {
            case Raw:
                taxCalculation = new RawTaxCalculation();
                break;

            case Manufactured:
                taxCalculation = new ManufactureTaxCalculation();
                break;

            case Imported:
                taxCalculation = new ImportedTaxCalculation();
                break;

            default:
                throw new IllegalArgumentException("Invalid item type");
        }

        // Calculate total price
        double totalPrice = item.price * item.quantity;

        // Calculate tax and final price
        double tax = taxCalculation.getTaxCalculate(totalPrice);
        double finalPrice = totalPrice + tax;

        return new double[]{tax, finalPrice};
    }
}

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Stores valid items
        ArrayList<Items> items = new ArrayList<>();

        // Stores item names and types for duplicate checking
        HashMap<String, ArrayList<Type>> itemMapWithType = new HashMap<>();

        while (true) {
            System.out.print("Do you want to enter details of any item (y/n): ");
            String response = sc.next().toLowerCase();

            // Exit the program
            if (response.equals("n") || response.equals("no")) {
                break;
            }

            // Validate yes/no response
            if (!response.equals("y") && !response.equals("yes")) {
                System.out.println("Invalid response. Please enter y or n.");
                continue;
            }

            try {
                // Read and validate item details
                Items item = readItem(sc, itemMapWithType);
                items.add(item);

                // Store item details for duplicate checking
                itemMapWithType.computeIfAbsent(item.name, k -> new ArrayList<>()).add(item.type);

                System.out.println("Item details accepted successfully.");

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        // Display all valid items
        for (int i = 0; i < items.size(); i++) {
            Items item = items.get(i);
            double[] prices = CalculateTaxOfItem.calculateTaxOfItem(item);
            double totalPrice = item.price * item.quantity;

            display(item, prices, totalPrice);
        }

        sc.close();
    }

    // Reads item details from the user
    public static Items readItem(Scanner sc, HashMap<String, ArrayList<Type>> itemMapWithType) {
        System.out.print("Enter item name: ");
        String name = sc.next();

        System.out.print("Enter item price: ");
        double price = sc.nextDouble();

        System.out.print("Enter item quantity: ");
        int qty = sc.nextInt();

        System.out.print("Enter item type (raw/manufactured/imported): ");
        String type = sc.next();

        Items item = new Items();
        item.name = name;
        item.price = price;
        item.quantity = qty;

        try {
            // Convert input type to lowercase
            String formattedType = type.trim().toLowerCase();

            switch (formattedType) {
                case "raw":
                    item.type = Type.Raw;
                    break;

                case "manufactured":
                    item.type = Type.Manufactured;
                    break;

                case "imported":
                    item.type = Type.Imported;
                    break;

                default:
                    throw new IllegalArgumentException("Invalid item type");
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid item type");
        }

        // Validate the complete item
        ValidateItem.validateItem(item, itemMapWithType);

        return item;
    }

    // Displays item details
    public static void display(Items item, double[] price, double totalPrice) {
        double tax = price[0];
        double finalPrice = price[1];

        System.out.println("\n----- Item Details -----");
        System.out.println("Item Name: " + item.name);
        System.out.println("Item Type: " + item.type);
        System.out.println("Item Price: " + item.price);
        System.out.println("Quantity: " + item.quantity);
        System.out.println("Total Price: " + totalPrice);
        System.out.println("Tax: " + tax);
        System.out.println("Final Price: " + finalPrice);
        System.out.println("------------------------\n");
    }
}
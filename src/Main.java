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
    public static void validateItem(Items item, HashMap<String, ArrayList<Type>> itemMapWithType) {
        validateName(item.name);
        validatePrice(item.price);
        validateQuantity(item.quantity);
        validateType(item.type);
        validateDuplicacy(itemMapWithType, item.name, item.type);
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
    }

    public static void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }

    public static void validateQuantity(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    public static void validateDuplicacy(
            HashMap<String, ArrayList<Type>> itemMapWithType,
            String name,
            Type type) {

        if (itemMapWithType.containsKey(name)
                && itemMapWithType.get(name).contains(type)) {
            throw new IllegalArgumentException("Same item already exists in DB");
        }
    }

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
        return totalPrice * 0.125;
    }
}

class ManufactureTaxCalculation implements TaxCalculation {
    @Override
    public double getTaxCalculate(double totalPrice) {
        double basicTax = totalPrice * 0.125;
        double additionalTax = (totalPrice + basicTax) * 0.02;

        return basicTax + additionalTax;
    }
}

class ImportedTaxCalculation implements TaxCalculation {
    @Override
    public double getTaxCalculate(double totalPrice) {
        double importDuty = totalPrice * 0.10;
        double finalCost = totalPrice + importDuty;
        double surcharge;

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

        double totalPrice = item.price * item.quantity;
        double tax = taxCalculation.getTaxCalculate(totalPrice);
        double finalPrice = totalPrice + tax;

        return new double[]{tax, finalPrice};
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Items> items = new ArrayList<>();
        HashMap<String, ArrayList<Type>> itemMapWithType = new HashMap<>();

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
                Items item = readItem(sc, itemMapWithType);
                items.add(item);

                itemMapWithType
                        .computeIfAbsent(item.name, k -> new ArrayList<>())
                        .add(item.type);

                System.out.println("Item details accepted successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        for (int i = 0; i < items.size(); i++) {
            Items item = items.get(i);
            double[] prices = CalculateTaxOfItem.calculateTaxOfItem(item);
            double totalPrice = item.price * item.quantity;

            display(item, prices, totalPrice);
        }

        sc.close();
    }

    public static Items readItem(
            Scanner sc,
            HashMap<String, ArrayList<Type>> itemMapWithType) {

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
            item.type = Type.valueOf(
                    type.substring(0, 1).toUpperCase()
                            + type.substring(1).toLowerCase()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid item type");
        }

        ValidateItem.validateItem(item, itemMapWithType);

        return item;
    }

    public static void display(
            Items item,
            double[] price,
            double totalPrice) {

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
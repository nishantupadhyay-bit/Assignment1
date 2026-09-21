import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import enums.Type;

class Items {
    private String name;
    private double price;
    private int quantity;
    private Type type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

class ValidateName {

    public static void validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
    }
}

class ValidatePrice {

    public static void validate(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }
}

class ValidateQuantity {

    public static void validate(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }
}

class ValidateType {

    public static void validate(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Item type cannot be null");
        }
    }
}

class ValidateDuplicacy {

    public static void validate(
            Map<String, ArrayList<Type>> itemMapWithType,
            String name,
            Type type) {

        if (itemMapWithType.containsKey(name)
                && itemMapWithType.get(name).contains(type)) {

            throw new IllegalArgumentException("Same item already exists in DB");
        }
    }
}

class ValidateItem {

    public static void validateItem(
            Items item,
            Map<String, ArrayList<Type>> itemMapWithType) {

        ValidateName.validate(item.getName());
        ValidatePrice.validate(item.getPrice());
        ValidateQuantity.validate(item.getQuantity());
        ValidateType.validate(item.getType());

        ValidateDuplicacy.validate(
                itemMapWithType,
                item.getName(),
                item.getType()
        );
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

        switch (item.getType()) {
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

        double totalPrice = item.getPrice() * item.getQuantity();
        double tax = taxCalculation.getTaxCalculate(totalPrice);
        double finalPrice = totalPrice + tax;

        return new double[]{tax, finalPrice};
    }
}

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
                Items item = readItem(sc, itemMapWithType);
                items.add(item);

                itemMapWithType
                        .computeIfAbsent(item.getName(), k -> new ArrayList<>())
                        .add(item.getType());

                System.out.println("Item details accepted successfully.");

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        for (int i = 0; i < items.size(); i++) {
            Items item = items.get(i);

            double[] prices = CalculateTaxOfItem.calculateTaxOfItem(item);
            double totalPrice = item.getPrice() * item.getQuantity();

            display(item, prices, totalPrice);
        }

        sc.close();
    }

    public static Items readItem(
            Scanner sc,
            Map<String, ArrayList<Type>> itemMapWithType) {

        System.out.print("Enter item name: ");
        String name = sc.next();

        System.out.print("Enter item price: ");
        double price = sc.nextDouble();

        System.out.print("Enter item quantity: ");
        int quantity = sc.nextInt();

        System.out.print("Enter item type (raw/manufactured/imported): ");
        String type = sc.next();

        Items item = new Items();

        item.setName(name);
        item.setPrice(price);
        item.setQuantity(quantity);

        String formattedType = type.trim().toLowerCase();

        switch (formattedType) {
            case "raw":
                item.setType(Type.Raw);
                break;

            case "manufactured":
                item.setType(Type.Manufactured);
                break;

            case "imported":
                item.setType(Type.Imported);
                break;

            default:
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
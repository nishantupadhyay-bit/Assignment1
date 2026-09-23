package input;

import enums.Type;
import item.Items;
import validation.ValidateItem;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class ItemInput {
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

        Type itemType;

        switch (type.trim().toLowerCase()) {
            case "raw":
                itemType = Type.Raw;
                break;
            case "manufactured":
                itemType = Type.Manufactured;
                break;
            case "imported":
                itemType = Type.Imported;
                break;
            default:
                throw new IllegalArgumentException("Invalid item type");
        }

        Items item = new Items(name, price, quantity, itemType);
        ValidateItem.validateItem(item, itemMapWithType);

        return item;
    }
}
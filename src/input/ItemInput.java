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
}
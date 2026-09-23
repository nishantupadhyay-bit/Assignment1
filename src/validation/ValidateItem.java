package validation;

import enums.Type;
import item.Items;

import java.util.ArrayList;
import java.util.Map;

public class ValidateItem {
    public static void validateItem(
            Items item,
            Map<String, ArrayList<Type>> itemMapWithType) {

        ValidateName.validate(item.getName());
        ValidatePrice.validate(item.getPrice());
        ValidateQuantity.validate(item.getQuantity());
        ValidateType.validate(item.getType());
        ValidateDuplicacy.validate(itemMapWithType, item.getName(), item.getType());
    }
}
package validation;

import enums.Type;

import java.util.ArrayList;
import java.util.Map;

public class ValidateDuplicacy {
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
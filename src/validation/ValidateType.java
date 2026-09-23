package validation;

import enums.Type;

public class ValidateType {
    public static void validate(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Item type cannot be null");
        }
    }
}
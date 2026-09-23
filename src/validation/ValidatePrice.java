package validation;

public class ValidatePrice {
    public static void validate(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }
}
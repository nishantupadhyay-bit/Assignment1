package test;

import enums.Type;
import item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tax.CalculateTaxOfItem;
import validation.ValidateItem;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class Assignment1UnitTest {
    private HashMap<String, ArrayList<Type>> itemMapWithType;
    private ValidateItem validateItem;
    private CalculateTaxOfItem calculateTaxOfItem;

    @BeforeEach
    void setUp() {
        itemMapWithType = new HashMap<>();
        validateItem = new ValidateItem();
        calculateTaxOfItem = new CalculateTaxOfItem();
    }

    @Test
    void testValidRawItem() {
        Items item = new Items("Rice", 100, 1, Type.Raw);

        assertDoesNotThrow(() ->
                validateItem.validateItem(item, itemMapWithType)
        );
    }

    @Test
    void testEmptyItemName() {
        Items item = new Items("", 100, 1, Type.Raw);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateItem.validateItem(item, itemMapWithType)
        );

        assertEquals("Item name cannot be empty", exception.getMessage());
    }

    @Test
    void testBlankItemName() {
        Items item = new Items("   ", 100, 1, Type.Raw);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateItem.validateItem(item, itemMapWithType)
        );

        assertEquals("Item name cannot be empty", exception.getMessage());
    }

    @Test
    void testNegativePrice() {
        Items item = new Items("Rice", -100, 1, Type.Raw);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateItem.validateItem(item, itemMapWithType)
        );

        assertEquals("Price must be greater than 0", exception.getMessage());
    }

    @Test
    void testZeroPrice() {
        Items item = new Items("Rice", 0, 1, Type.Raw);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateItem.validateItem(item, itemMapWithType)
        );

        assertEquals("Price must be greater than 0", exception.getMessage());
    }

    @Test
    void testNegativeQuantity() {
        Items item = new Items("Rice", 100, -2, Type.Raw);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateItem.validateItem(item, itemMapWithType)
        );

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

    @Test
    void testZeroQuantity() {
        Items item = new Items("Rice", 100, 0, Type.Raw);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateItem.validateItem(item, itemMapWithType)
        );

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

    @Test
    void testNullItemType() {
        Items item = new Items("Rice", 100, 1, null);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateItem.validateItem(item, itemMapWithType)
        );

        assertEquals("Item type cannot be null", exception.getMessage());
    }

    @Test
    void testDuplicateItemWithSameType() {
        Items firstItem = new Items("Rice", 100, 1, Type.Raw);
        Items secondItem = new Items("Rice", 200, 2, Type.Raw);

        validateItem.validateItem(firstItem, itemMapWithType);

        itemMapWithType
                .computeIfAbsent(firstItem.getName(), k -> new ArrayList<>())
                .add(firstItem.getType());

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateItem.validateItem(secondItem, itemMapWithType)
        );

        assertEquals("Same item already exists in DB", exception.getMessage());
    }

    @Test
    void testSameNameWithDifferentType() {
        Items rawItem = new Items("Rice", 100, 1, Type.Raw);
        Items importedItem = new Items("Rice", 100, 1, Type.Imported);

        assertDoesNotThrow(() ->
                validateItem.validateItem(rawItem, itemMapWithType)
        );

        itemMapWithType
                .computeIfAbsent(rawItem.getName(), k -> new ArrayList<>())
                .add(rawItem.getType());

        assertDoesNotThrow(() ->
                validateItem.validateItem(importedItem, itemMapWithType)
        );
    }

    @Test
    void testRawTaxCalculation() {
        Items item = new Items("Rice", 100, 1, Type.Raw);

        double[] result = calculateTaxOfItem.calculateTaxOfItem(item);

        assertEquals(12.5, result[0], 0.001);
        assertEquals(112.5, result[1], 0.001);
    }

    @Test
    void testManufacturedTaxCalculation() {
        Items item = new Items("Laptop", 100, 1, Type.Manufactured);

        double[] result = calculateTaxOfItem.calculateTaxOfItem(item);

        assertEquals(14.75, result[0], 0.001);
        assertEquals(114.75, result[1], 0.001);
    }

    @Test
    void testImportedTaxLowSurcharge() {
        Items item = new Items("Car", 50, 1, Type.Imported);

        double[] result = calculateTaxOfItem.calculateTaxOfItem(item);

        assertEquals(10.0, result[0], 0.001);
        assertEquals(60.0, result[1], 0.001);
    }

    @Test
    void testImportedTaxMediumSurcharge() {
        Items item = new Items("Car", 150, 1, Type.Imported);

        double[] result = calculateTaxOfItem.calculateTaxOfItem(item);

        assertEquals(25.0, result[0], 0.001);
        assertEquals(175.0, result[1], 0.001);
    }

    @Test
    void testImportedTaxHighSurcharge() {
        Items item = new Items("Car", 200, 1, Type.Imported);

        double[] result = calculateTaxOfItem.calculateTaxOfItem(item);

        assertEquals(31.0, result[0], 0.001);
        assertEquals(231.0, result[1], 0.001);
    }

    @Test
    void testQuantityBasedCalculation() {
        Items item = new Items("Rice", 100, 3, Type.Raw);

        double[] result = calculateTaxOfItem.calculateTaxOfItem(item);

        assertEquals(37.5, result[0], 0.001);
        assertEquals(337.5, result[1], 0.001);
    }
}
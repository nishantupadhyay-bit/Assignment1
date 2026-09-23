package tax;

import item.Items;

public class CalculateTaxOfItem {
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
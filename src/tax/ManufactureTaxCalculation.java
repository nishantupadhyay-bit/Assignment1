package tax;

public class ManufactureTaxCalculation implements TaxCalculation {
    @Override
    public double getTaxCalculate(double totalPrice) {
        double basicTax = totalPrice * 0.125;
        double additionalTax = (totalPrice + basicTax) * 0.02;

        return basicTax + additionalTax;
    }
}
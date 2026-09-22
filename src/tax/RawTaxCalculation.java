package tax;

public class RawTaxCalculation implements TaxCalculation {
    @Override
    public double getTaxCalculate(double totalPrice) {
        return totalPrice * 0.125;
    }
}
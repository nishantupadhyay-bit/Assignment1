package tax;

public class ImportedTaxCalculation implements TaxCalculation {
    @Override
    public double getTaxCalculate(double totalPrice) {
        double importDuty = totalPrice * 0.10;
        double finalCost = totalPrice + importDuty;
        double surcharge;

        if (finalCost <= 100) {
            surcharge = 5;
        } else if (finalCost <= 200) {
            surcharge = 10;
        } else {
            surcharge = finalCost * 0.05;
        }

        return importDuty + surcharge;
    }
}
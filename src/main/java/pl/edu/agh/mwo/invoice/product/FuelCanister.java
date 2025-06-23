package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class FuelCanister extends Product {
    private static final BigDecimal EXCISE_FEE = new BigDecimal("5.56");

    public FuelCanister(String name, BigDecimal price) {
        // VAT na paliwa ciekłe zniesiony → taxPercent = 0
        super(name, price, BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getPriceWithTax() {
        // cena netto + akcyza (bez VAT)
        return getPrice().add(EXCISE_FEE);
    }
}
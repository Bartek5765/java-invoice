package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class BottleOfWine extends Product {
    private static final BigDecimal EXCISE_FEE = new BigDecimal("5.56");

    public BottleOfWine(String name, BigDecimal price) {
        // VAT 23%
        super(name, price, new BigDecimal("0.23"));
    }

    @Override
    public BigDecimal getPriceWithTax() {
        // cena netto + VAT + akcyza
        return super.getPriceWithTax().add(EXCISE_FEE);
    }
}
package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testInvoiceHasPositiveNumber() {
        // nowa faktura powinna mieć numer > 0
        Assert.assertTrue(invoice.getNumber() > 0);
    }
    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
        // nowa fatura powinna mieć różne numery
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertNotEquals(number1, number2);
    }

    @Test
    public void testInvoiceDoesNotChangeItsNumber() {
        // numer faktury nie zmienia się
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }

    @Test
    public void testTheFirstInvoiceNumberIsLowerThanTheSecond() {
        // porządek numeracji
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertThat(number1, Matchers.lessThan(number2));
    }

    @Test
    public void testPrintEmptyInvoice() {
        String expected = "Numer faktury: " + invoice.getNumber() + "\nLiczba pozycji: 0";
        Assert.assertEquals(expected, invoice.getPrintableInvoice());
    }

    @Test
    public void testPrintInvoiceWithProducts() {
        Product p1 = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product p2 = new DairyProduct("Maslo", new BigDecimal("5"));
        invoice.addProduct(p1, 2);
        invoice.addProduct(p2);

        String[] lines = invoice.getPrintableInvoice().split("\n");
        Assert.assertEquals("Numer faktury: " + invoice.getNumber(), lines[0]);
        Assert.assertEquals("Warzywa | 2 | 10",            lines[1]);
        Assert.assertEquals("Maslo | 1 | 5",               lines[2]);
        Assert.assertEquals("Liczba pozycji: 2",           lines[3]);
    }

    @Test
    public void testPrintInvoiceWithDuplicateProducts() {
        TaxFreeProduct tea = new TaxFreeProduct("Herbata", new BigDecimal("3"));
        invoice.addProduct(tea, 2);
        invoice.addProduct(tea, 3);

        String[] lines = invoice.getPrintableInvoice().split("\n");
        // powinien być tylko jeden wiersz dla Herbaty
        Assert.assertEquals(1, lines.length - 2); // numer + pozycje + liczbapozycji
        Assert.assertEquals("Herbata | 5 | 3", lines[1]);
        Assert.assertEquals("Liczba pozycji: 1", lines[2]);
    }

    @Test
    public void testNetTotalWithDuplicateProducts() {
        TaxFreeProduct sugar = new TaxFreeProduct("Cukier", new BigDecimal("2"));
        invoice.addProduct(sugar, 4);
        invoice.addProduct(sugar, 6);
        // cena netto 2 * (4+6) = 20
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(new BigDecimal("20")));
    }

    @Test
    public void testGrossTotalWithDuplicateProducts() {
        DairyProduct milk = new DairyProduct("Mleko", new BigDecimal("5"));
        // podatek 8% więc cena z podatkiem = 5.40
        invoice.addProduct(milk, 1);
        invoice.addProduct(milk, 1);
        // 2 * 5.40 = 10.80
        Assert.assertThat(invoice.getGrossTotal(), Matchers.comparesEqualTo(new BigDecimal("10.80")));
    }

    @Test
    public void testInvoiceTotalsWithWineExcise() {
        // 2 butelki wina po 10 zł netto: 2×(10+23%) + 2×5.56 = 2×12.30 + 11.12 = 24.60 + 11.12 = 35.72
        invoice.addProduct(new BottleOfWine("Wino", new BigDecimal("10.00")), 2);
        Assert.assertThat(invoice.getNetTotal(),    Matchers.comparesEqualTo(new BigDecimal("20.00")));
        Assert.assertThat(invoice.getGrossTotal(),  Matchers.comparesEqualTo(new BigDecimal("35.72")));
        Assert.assertThat(invoice.getTaxTotal(),    Matchers.comparesEqualTo(new BigDecimal("15.72")));
    }

    @Test
    public void testInvoiceTotalsWithFuelExciseOnly() {
        // 3 kanistry paliwa po 4 zł netto: 3×(4 + 5.56) = 3×9.56 = 28.68
        invoice.addProduct(new FuelCanister("ON", new BigDecimal("4.00")), 3);
        Assert.assertThat(invoice.getNetTotal(),    Matchers.comparesEqualTo(new BigDecimal("12.00")));
        Assert.assertThat(invoice.getGrossTotal(),  Matchers.comparesEqualTo(new BigDecimal("28.68")));
        Assert.assertThat(invoice.getTaxTotal(),    Matchers.comparesEqualTo(new BigDecimal("16.68")));
    }

}

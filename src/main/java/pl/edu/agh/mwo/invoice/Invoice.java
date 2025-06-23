package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    // licznik numerów faktur
    private static int invoiceCounter = 0;

    // numer faktury
    private final int number;

    private Map<Product, Integer> products = new LinkedHashMap<>();

    // inicjalizacja numeru
    public Invoice() {
        this.number = ++invoiceCounter;
    }

    public int getNumber() {
        return number;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        int existing = products.getOrDefault(product, 0);
        products.put(product, existing + quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            BigDecimal qty = new BigDecimal(entry.getValue());
            totalNet = totalNet.add(entry.getKey().getPrice().multiply(qty));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            BigDecimal qty = new BigDecimal(entry.getValue());
            totalGross = totalGross.add(entry.getKey().getPriceWithTax().multiply(qty));
        }
        return totalGross;
    }

    public String getPrintableInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("Numer faktury: ").append(number).append("\n");
        for (Map.Entry<Product, Integer> e : products.entrySet()) {
            sb.append(e.getKey().getName())
                    .append(" | ").append(e.getValue())
                    .append(" | ").append(e.getKey().getPrice())
                    .append("\n");
        }
        sb.append("Liczba pozycji: ").append(products.size());
        return sb.toString();
    }

}

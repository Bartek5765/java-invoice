package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    // licznik numerów faktur
    private static int invoiceCounter = 0;

    // numer faktury
    private final int number;

    private Map<Product, Integer> products = new HashMap<Product, Integer>();

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
        products.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public String getPrintableInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("Numer faktury: ").append(number)
                .append("\n");
        if (!products.isEmpty()) {
            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                Product product = entry.getKey();
                int qty = entry.getValue();
                sb.append(product.getName())
                        .append(" | ")
                        .append(qty)
                        .append(" | ")
                        .append(product.getPrice())
                        .append("\n");
            }
        }
        sb.append("Liczba pozycji: ").append(products.size());
        return sb.toString();
    }

}

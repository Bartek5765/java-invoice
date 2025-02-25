package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    // private Collection<Product> products = new ArrayList<>();

    private final Map<Product, Integer> products = new HashMap<>();

    public void addProduct(Product product) {
        addProduct(product, 1);

    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        products.merge(product, quantity, Integer::sum);
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            BigDecimal priceNet = entry.getKey().getPrice();
            int quantity = entry.getValue();
            subtotal = subtotal.add(priceNet.multiply(BigDecimal.valueOf(quantity)));
        }
        return subtotal;
    }

    public BigDecimal getTax() {
        BigDecimal totalTax = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            BigDecimal taxPerOne = entry.getKey().getPriceWithTax().subtract(entry.getKey().getPrice());
            int quantity = entry.getValue();
            totalTax = totalTax.add(taxPerOne.multiply(BigDecimal.valueOf(quantity)));
        }
        return totalTax;
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getTax());
    }
}

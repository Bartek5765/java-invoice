package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Scanner;

import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;
import pl.edu.agh.mwo.invoice.product.Product;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Invoice invoice = new Invoice();
        System.out.println("Tworzenie nowej faktury numer: " + invoice.getNumber());

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Wybierz opcję:");
            System.out.println("1. Dodaj produkt");
            System.out.println("2. Drukuj fakturę");
            System.out.println("3. Wyjście");
            System.out.print("Twój wybór: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.print("Rodzaj produktu (1: TaxFree, 2: Dairy, 3: Other): ");
                    int type = Integer.parseInt(scanner.nextLine());

                    System.out.print("Nazwa: ");
                    String name = scanner.nextLine();

                    System.out.print("Cena netto: ");
                    BigDecimal price = new BigDecimal(scanner.nextLine());

                    System.out.print("Ilość: ");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    Product product;
                    switch (type) {
                        case 2:
                            product = new DairyProduct(name, price);
                            break;
                        case 3:
                            product = new OtherProduct(name, price);
                            break;
                        default:
                            product = new TaxFreeProduct(name, price);
                    }
                    invoice.addProduct(product, quantity);
                    System.out.println("Dodano produkt: " + name + " x" + quantity);
                    break;

                case "2":
                    System.out.println();
                    System.out.println(invoice.getPrintableInvoice());
                    break;

                case "3":
                    running = false;
                    System.out.println("Koniec programu.");
                    break;

                default:
                    System.out.println("Nieprawidłowa opcja. Spróbuj ponownie.");
            }
        }

        scanner.close();
    }
}

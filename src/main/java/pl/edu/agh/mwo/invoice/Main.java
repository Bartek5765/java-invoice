package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Scanner;

import pl.edu.agh.mwo.invoice.product.*;

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
            System.out.println("3. Pokaż podsumowanie");
            System.out.println("4. Wyjście");
            System.out.print("Twój wybór: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.println("Rodzaj produktu:");
                    System.out.println("1: TaxFree, 2: Dairy, 3: Other, 4: Wine, 5: Fuel");
                    System.out.print("Wybierz typ (1-5): ");
                    int type = Integer.parseInt(scanner.nextLine());

                    System.out.print("Nazwa: ");
                    String name = scanner.nextLine();

                    System.out.print("Cena netto: ");
                    BigDecimal price = new BigDecimal(scanner.nextLine());

                    System.out.print("Ilość: ");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    Product product;
                    switch (type) {
                        case 2: product = new DairyProduct(name, price); break;
                        case 3: product = new OtherProduct(name, price); break;
                        case 4: product = new BottleOfWine(name, price); break;
                        case 5: product = new FuelCanister(name, price); break;
                        default: product = new TaxFreeProduct(name, price);
                    }
                    invoice.addProduct(product, quantity);
                    System.out.println("Dodano produkt: " + product.getName() + " x" + quantity);
                    break;

                case "2":
                    System.out.println();
                    System.out.println(invoice.getPrintableInvoice());
                    break;

                case "3":
                    System.out.println();
                    System.out.println("=== PODSUMOWANIE FAKTURY ===");
                    System.out.println("Suma netto:           " + invoice.getNetTotal());
                    System.out.println("Suma podatku+akcyzy:  " + invoice.getTaxTotal());
                    System.out.println("Suma brutto:          " + invoice.getGrossTotal());
                    break;

                case "4":
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

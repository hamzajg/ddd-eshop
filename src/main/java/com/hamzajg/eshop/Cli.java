package com.hamzajg.eshop;

import com.hamzajg.eshop.application.ordering.CancelOrder;
import com.hamzajg.eshop.application.ordering.ConfirmOrder;
import com.hamzajg.eshop.application.ordering.PlaceOrder;
import com.hamzajg.eshop.application.ordering.ShipOrder;
import com.hamzajg.eshop.domain.catalog.Product;
import com.hamzajg.eshop.domain.ordering.Order;
import com.hamzajg.eshop.domain.ordering.OrderLine;
import com.hamzajg.eshop.domain.shared.Money;
import com.hamzajg.eshop.infrastructure.InMemoryCatalogRepository;
import com.hamzajg.eshop.infrastructure.InMemoryOrderRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal interactive shell over the use cases — the repo's runnable
 * entry point until a real transport (HTTP) exists. Seeds a small demo
 * catalog, then reads one command per line:
 *
 *   catalog                      list products
 *   place <num> <SKU:qty>...     place an order
 *   confirm|ship|cancel <num>    order lifecycle
 *   show <num>                   order status, lines, total
 *   quit                         exit
 */
public final class Cli {

    private static final String USAGE = """
            commands: catalog | place <num> <SKU:qty>... | confirm <num> |
            ship <num> | cancel <num> | show <num> | quit""";

    private final InMemoryCatalogRepository catalog = new InMemoryCatalogRepository();
    private final InMemoryOrderRepository orders = new InMemoryOrderRepository();
    private final PlaceOrder placeOrder;
    private final ConfirmOrder confirmOrder;
    private final ShipOrder shipOrder;
    private final CancelOrder cancelOrder;

    public Cli() {
        seedCatalog();
        placeOrder = new PlaceOrder(catalog, orders);
        confirmOrder = new ConfirmOrder(orders);
        shipOrder = new ShipOrder(orders);
        cancelOrder = new CancelOrder(orders);
    }

    private void seedCatalog() {
        catalog.add(new Product("KB-1", "Mechanical Keyboard", new Money(4999, "EUR")));
        catalog.add(new Product("MS-1", "Wireless Mouse", new Money(1999, "EUR")));
        catalog.add(new Product("MON-27", "27-inch Monitor", new Money(18999, "EUR")));
    }

    /** Executes one command line; returns the text to print. */
    public String dispatch(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            return "";
        }
        try {
            return switch (parts[0]) {
                case "catalog" -> catalogText();
                case "place" -> place(parts);
                case "confirm" -> confirmOrder.execute(arg(parts)) + " " + arg(parts);
                case "ship" -> shipOrder.execute(arg(parts)) + " " + arg(parts);
                case "cancel" -> cancelOrder.execute(arg(parts)) + " " + arg(parts);
                case "show" -> show(arg(parts));
                case "quit", "exit" -> null;
                default -> "unknown command: " + parts[0] + "\n" + USAGE;
            };
        } catch (RuntimeException e) {
            return "error: " + e.getMessage();
        }
    }

    private static String arg(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException("missing order number");
        }
        return parts[1];
    }

    private String catalogText() {
        StringBuilder sb = new StringBuilder("catalog:");
        for (Product p : catalog.all()) {
            sb.append("\n  ").append(p.sku()).append("  ")
                    .append(p.name()).append("  ")
                    .append(p.price().amountMinor() / 100.0)
                    .append(' ').append(p.price().currency());
        }
        return sb.toString();
    }

    private String place(String[] parts) {
        if (parts.length < 3) {
            throw new IllegalArgumentException("usage: place <num> <SKU:qty>...");
        }
        List<PlaceOrder.LineRequest> lines = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            String[] kv = parts[i].split(":");
            if (kv.length != 2) {
                throw new IllegalArgumentException("bad item '" + parts[i] + "', expected SKU:qty");
            }
            lines.add(new PlaceOrder.LineRequest(kv[0], Integer.parseInt(kv[1])));
        }
        PlaceOrder.Result result = placeOrder.execute(
                new PlaceOrder.Command(parts[1], lines));
        return "order " + result.orderNumber() + " placed, total "
                + result.total().amountMinor() / 100.0 + " " + result.total().currency();
    }

    private String show(String orderNumber) {
        Order order = orders.findByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("unknown order: " + orderNumber));
        StringBuilder sb = new StringBuilder("order ").append(order.orderNumber())
                .append(" [").append(order.status()).append("]");
        for (OrderLine line : order.lines()) {
            sb.append("\n  ").append(line.sku()).append(" x").append(line.quantity())
                    .append("  ").append(line.unitPrice().amountMinor() / 100.0);
        }
        Money total = order.total();
        sb.append("\n  total ").append(total.amountMinor() / 100.0).append(' ').append(total.currency());
        return sb.toString();
    }

    public static void main(String[] args) {
        Cli cli = new Cli();
        if (args.length > 0) {
            String out = cli.dispatch(String.join(" ", args));
            if (out != null) {
                System.out.println(out);
            }
            return;
        }
        System.out.println("ddd-eshop demo — " + USAGE);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                String out = cli.dispatch(line);
                if (out == null) {
                    break;
                }
                if (!out.isEmpty()) {
                    System.out.println(out);
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("io error: " + e.getMessage());
        }
    }
}

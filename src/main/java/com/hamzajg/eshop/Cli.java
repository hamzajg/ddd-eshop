package com.hamzajg.eshop;

import com.hamzajg.eshop.application.customer.LoginCustomer;
import com.hamzajg.eshop.application.ordering.CancelOrder;
import com.hamzajg.eshop.application.ordering.ConfirmOrder;
import com.hamzajg.eshop.application.ordering.ListOrders;
import com.hamzajg.eshop.application.ordering.PlaceOrder;
import com.hamzajg.eshop.application.ordering.ShipOrder;
import com.hamzajg.eshop.domain.catalog.Product;
import com.hamzajg.eshop.domain.ordering.Order;
import com.hamzajg.eshop.domain.ordering.OrderLine;
import com.hamzajg.eshop.domain.shared.Money;
import com.hamzajg.eshop.infrastructure.InMemoryCatalogRepository;
import com.hamzajg.eshop.infrastructure.InMemoryCustomerRepository;
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
 *   login <name>                  log in (registers on first use)
 *   logout                        end the session
 *   catalog                      list products
 *   place <num> <SKU:qty>...     place an order
 *   confirm|ship|cancel <num>    order lifecycle
 *   show <num>                   order status, lines, total
 *   quit                         exit
 */
public final class Cli {

    private static final String USAGE = """
            commands: login <name> | logout | catalog | place <num> <SKU:qty>... |
            confirm <num> | ship <num> | cancel <num> | show <num> | quit""";

    private final InMemoryCatalogRepository catalog = new InMemoryCatalogRepository();
    private final InMemoryCustomerRepository customers = new InMemoryCustomerRepository();
    private final InMemoryOrderRepository orders = new InMemoryOrderRepository();
    private final PlaceOrder placeOrder;
    private final ConfirmOrder confirmOrder;
    private final ShipOrder shipOrder;
    private final CancelOrder cancelOrder;
    private final ListOrders listOrders;
    private final LoginCustomer loginCustomer;
    private String sessionCustomer;

    public Cli() {
        seedCatalog();
        placeOrder = new PlaceOrder(catalog, orders);
        confirmOrder = new ConfirmOrder(orders);
        shipOrder = new ShipOrder(orders);
        cancelOrder = new CancelOrder(orders);
        listOrders = new ListOrders(orders);
        loginCustomer = new LoginCustomer(customers);
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
                case "login" -> login(parts);
                case "logout" -> logout();
                case "catalog" -> catalogText();
                case "place" -> place(parts);
                case "confirm" -> confirmOrder.execute(arg(parts)) + " " + arg(parts);
                case "ship" -> shipOrder.execute(arg(parts)) + " " + arg(parts);
                case "cancel" -> cancelOrder.execute(arg(parts)) + " " + arg(parts);
                case "show" -> show(arg(parts));
                case "orders" -> orders();
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

    private String login(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException("usage: login <name>");
        }
        LoginCustomer.Result result = loginCustomer.execute(
                new LoginCustomer.Command(parts[1]));
        sessionCustomer = result.name();
        return result.created()
                ? "welcome, " + result.name() + " — happy shopping"
                : "welcome back, " + result.name();
    }

    private String logout() {
        if (sessionCustomer == null) {
            return "not logged in";
        }
        String name = sessionCustomer;
        sessionCustomer = null;
        return "logged out " + name;
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
        if (sessionCustomer == null) {
            throw new IllegalStateException("please log in to place an order");
        }
        PlaceOrder.Result result = placeOrder.execute(
                new PlaceOrder.Command(parts[1], sessionCustomer, lines));
        return "order " + result.orderNumber() + " placed, total "
                + result.total().amountMinor() / 100.0 + " " + result.total().currency();
    }

    private String orders() {
        if (sessionCustomer == null) {
            throw new IllegalStateException("please log in to see your orders");
        }
        List<Order> mine = listOrders.execute(sessionCustomer);
        if (mine.isEmpty()) {
            return "no orders yet — happy shopping, " + sessionCustomer;
        }
        StringBuilder sb = new StringBuilder("your orders:");
        for (Order order : mine) {
            Money total = order.total();
            sb.append("\n  ").append(order.orderNumber())
                    .append(" [").append(order.status()).append("]  ")
                    .append(total.amountMinor() / 100.0).append(' ')
                    .append(total.currency());
        }
        return sb.toString();
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

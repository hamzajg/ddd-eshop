package com.hamzajg.eshop.application.ordering;

import com.hamzajg.eshop.domain.catalog.CatalogRepository;
import com.hamzajg.eshop.domain.catalog.Product;
import com.hamzajg.eshop.domain.ordering.Order;
import com.hamzajg.eshop.domain.ordering.OrderLine;
import com.hamzajg.eshop.domain.ordering.OrderRepository;
import com.hamzajg.eshop.domain.shared.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Places an order: resolves each requested SKU against the catalog,
 * snapshots name/price into order lines, persists the new order.
 * Rejects unknown or inactive products and reused order numbers.
 */
public final class PlaceOrder {

    public record LineRequest(String sku, int quantity) {
    }

    public record Command(String orderNumber, List<LineRequest> lines) {
    }

    public record Result(String orderNumber, Money total) {
    }

    private final CatalogRepository catalog;
    private final OrderRepository orders;

    public PlaceOrder(CatalogRepository catalog, OrderRepository orders) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        this.orders = Objects.requireNonNull(orders, "orders");
    }

    public Result execute(Command command) {
        Objects.requireNonNull(command, "command");
        if (orders.findByNumber(command.orderNumber()).isPresent()) {
            throw new IllegalStateException(
                    "order number already used: " + command.orderNumber());
        }

        List<OrderLine> lines = new ArrayList<>();
        for (LineRequest request : command.lines()) {
            lines.add(toOrderLine(request));
        }

        Order order = Order.place(command.orderNumber(), lines);
        orders.save(order);
        return new Result(order.orderNumber(), order.total());
    }

    private OrderLine toOrderLine(LineRequest request) {
        Product product = catalog.findBySku(request.sku())
                .orElseThrow(() -> new IllegalArgumentException(
                        "unknown sku: " + request.sku()));
        if (!product.isActive()) {
            throw new IllegalStateException(
                    "product is not orderable: " + request.sku());
        }
        return new OrderLine(
                product.sku(), product.name(), product.price(), request.quantity());
    }
}

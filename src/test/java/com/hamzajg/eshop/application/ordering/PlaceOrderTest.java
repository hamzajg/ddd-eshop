package com.hamzajg.eshop.application.ordering;

import com.hamzajg.eshop.domain.catalog.Product;
import com.hamzajg.eshop.domain.ordering.OrderStatus;
import com.hamzajg.eshop.domain.shared.Money;
import com.hamzajg.eshop.infrastructure.InMemoryCatalogRepository;
import com.hamzajg.eshop.infrastructure.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderTest {

    private InMemoryCatalogRepository catalog;
    private InMemoryOrderRepository orders;
    private PlaceOrder placeOrder;

    @BeforeEach
    void setUp() {
        catalog = new InMemoryCatalogRepository();
        catalog.add(new Product("SKU-A", "Keyboard", new Money(4999, "EUR")));
        catalog.add(new Product("SKU-B", "Mouse", new Money(1999, "EUR")));
        Product inactive = new Product("SKU-X", "Discontinued", new Money(100, "EUR"));
        inactive.deactivate();
        catalog.add(inactive);
        orders = new InMemoryOrderRepository();
        placeOrder = new PlaceOrder(catalog, orders);
    }

    private PlaceOrder.Command command(String number, String... skusAndQty) {
        List<PlaceOrder.LineRequest> lines = new java.util.ArrayList<>();
        for (int i = 0; i < skusAndQty.length; i += 2) {
            lines.add(new PlaceOrder.LineRequest(skusAndQty[i], Integer.parseInt(skusAndQty[i + 1])));
        }
        return new PlaceOrder.Command(number, lines);
    }

    @Test
    void placesOrderWithCatalogPrices() {
        PlaceOrder.Result result = placeOrder.execute(
                command("ORD-1", "SKU-A", "2", "SKU-B", "1"));

        assertEquals("ORD-1", result.orderNumber());
        // 2 * 4999 + 1 * 1999 = 11997
        assertEquals(new Money(11997, "EUR"), result.total());

        var order = orders.findByNumber("ORD-1").orElseThrow();
        assertEquals(OrderStatus.PLACED, order.status());
        assertEquals("Keyboard", order.lines().get(0).description());
        assertEquals(new Money(4999, "EUR"), order.lines().get(0).unitPrice());
    }

    @Test
    void rejectsUnknownSku() {
        assertThrows(IllegalArgumentException.class,
                () -> placeOrder.execute(command("ORD-2", "NOPE", "1")));
        assertTrue(orders.findByNumber("ORD-2").isEmpty());
    }

    @Test
    void rejectsInactiveProduct() {
        assertThrows(IllegalStateException.class,
                () -> placeOrder.execute(command("ORD-3", "SKU-X", "1")));
    }

    @Test
    void rejectsReusedOrderNumber() {
        placeOrder.execute(command("ORD-4", "SKU-A", "1"));
        assertThrows(IllegalStateException.class,
                () -> placeOrder.execute(command("ORD-4", "SKU-B", "1")));
    }
}

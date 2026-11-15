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

class OrderLifecycleUseCasesTest {

    private InMemoryOrderRepository orders;
    private PlaceOrder placeOrder;
    private ConfirmOrder confirmOrder;
    private ShipOrder shipOrder;
    private CancelOrder cancelOrder;

    @BeforeEach
    void setUp() {
        InMemoryCatalogRepository catalog = new InMemoryCatalogRepository();
        catalog.add(new Product("SKU-A", "Keyboard", new Money(4999, "EUR")));
        orders = new InMemoryOrderRepository();
        placeOrder = new PlaceOrder(catalog, orders);
        confirmOrder = new ConfirmOrder(orders);
        shipOrder = new ShipOrder(orders);
        cancelOrder = new CancelOrder(orders);
    }

    private void place(String number) {
        placeOrder.execute(new PlaceOrder.Command(number, "alice",
                List.of(new PlaceOrder.LineRequest("SKU-A", 1))));
    }

    @Test
    void confirmThenShipWalksTheLifecycle() {
        place("ORD-1");
        assertEquals("CONFIRMED", confirmOrder.execute("ORD-1"));
        assertEquals("SHIPPED", shipOrder.execute("ORD-1"));
        assertEquals(OrderStatus.SHIPPED, orders.findByNumber("ORD-1").orElseThrow().status());
    }

    @Test
    void cancelFromPlacedAndConfirmed() {
        place("ORD-2");
        assertEquals("CANCELLED", cancelOrder.execute("ORD-2"));

        place("ORD-3");
        confirmOrder.execute("ORD-3");
        assertEquals("CANCELLED", cancelOrder.execute("ORD-3"));
    }

    @Test
    void domainRulesPropagateThroughUseCases() {
        place("ORD-4");
        // ship before confirm → domain rejects
        assertThrows(IllegalStateException.class, () -> shipOrder.execute("ORD-4"));
        // unknown order → use case rejects
        assertThrows(IllegalArgumentException.class, () -> confirmOrder.execute("NOPE"));
        // shipped order cannot be cancelled
        confirmOrder.execute("ORD-4");
        shipOrder.execute("ORD-4");
        assertThrows(IllegalStateException.class, () -> cancelOrder.execute("ORD-4"));
    }
}

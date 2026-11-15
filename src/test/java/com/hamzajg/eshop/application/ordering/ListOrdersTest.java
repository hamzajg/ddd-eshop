package com.hamzajg.eshop.application.ordering;

import com.hamzajg.eshop.domain.catalog.Product;
import com.hamzajg.eshop.domain.ordering.Order;
import com.hamzajg.eshop.domain.ordering.OrderLine;
import com.hamzajg.eshop.domain.shared.Money;
import com.hamzajg.eshop.infrastructure.InMemoryCatalogRepository;
import com.hamzajg.eshop.infrastructure.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOrdersTest {

    private InMemoryOrderRepository orders;
    private ListOrders listOrders;

    @BeforeEach
    void setUp() {
        orders = new InMemoryOrderRepository();
        listOrders = new ListOrders(orders);
        save("1001", "alice");
        save("2001", "bob");
        save("1002", "alice");
    }

    private void save(String number, String customer) {
        Order order = Order.place(number, customer, List.of(
                new OrderLine("SKU", "thing", new Money(100, "EUR"), 1)));
        orders.save(order);
    }

    @Test
    void returnsOnlyCustomersOrders() {
        List<Order> mine = listOrders.execute("alice");
        assertEquals(2, mine.size());
        assertTrue(mine.stream().allMatch(o -> o.customerName().equals("alice")));
    }

    @Test
    void emptyForUnknownCustomer() {
        assertTrue(listOrders.execute("carol").isEmpty());
    }
}

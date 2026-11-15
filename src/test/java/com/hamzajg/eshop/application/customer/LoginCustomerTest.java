package com.hamzajg.eshop.application.customer;

import com.hamzajg.eshop.domain.customer.Customer;
import com.hamzajg.eshop.infrastructure.InMemoryCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginCustomerTest {

    private InMemoryCustomerRepository customers;
    private LoginCustomer login;

    @BeforeEach
    void setUp() {
        customers = new InMemoryCustomerRepository();
        login = new LoginCustomer(customers);
    }

    @Test
    void firstLoginRegistersCustomer() {
        LoginCustomer.Result result = login.execute(new LoginCustomer.Command("alice"));
        assertEquals("alice", result.name());
        assertTrue(result.created());
        assertTrue(customers.findByName("alice").isPresent());
    }

    @Test
    void repeatLoginReusesSameCustomer() {
        login.execute(new LoginCustomer.Command("alice"));
        LoginCustomer.Result again = login.execute(new LoginCustomer.Command("alice"));
        assertFalse(again.created());
        assertEquals(1, customers.all().size());
    }

    @Test
    void blankNameRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> login.execute(new LoginCustomer.Command("  ")));
    }
}

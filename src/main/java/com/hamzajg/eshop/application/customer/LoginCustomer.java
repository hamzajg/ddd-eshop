package com.hamzajg.eshop.application.customer;

import com.hamzajg.eshop.domain.customer.Customer;
import com.hamzajg.eshop.domain.customer.CustomerRepository;

import java.util.Objects;

/**
 * Logs a shopper in by name: registers the customer on first login,
 * reuses the existing one afterwards. Name-as-identity is the MVP
 * contract — no credentials.
 */
public final class LoginCustomer {

    public record Command(String name) {
    }

    public record Result(String name, boolean created) {
    }

    private final CustomerRepository customers;

    public LoginCustomer(CustomerRepository customers) {
        this.customers = Objects.requireNonNull(customers, "customers");
    }

    public Result execute(Command command) {
        Objects.requireNonNull(command, "command");
        String name = command.name() == null ? "" : command.name().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("customer name must not be blank");
        }
        return customers.findByName(name)
                .map(customer -> new Result(customer.name(), false))
                .orElseGet(() -> {
                    customers.save(new Customer(name));
                    return new Result(name, true);
                });
    }
}

package com.hamzajg.eshop.infrastructure;

import com.hamzajg.eshop.domain.customer.Customer;
import com.hamzajg.eshop.domain.customer.CustomerRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** In-memory customer store for tests and server-free dev runs. */
public final class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> byName = new HashMap<>();

    @Override
    public void save(Customer customer) {
        byName.put(customer.name(), customer);
    }

    @Override
    public Optional<Customer> findByName(String name) {
        return Optional.ofNullable(byName.get(name));
    }

    @Override
    public List<Customer> all() {
        return new ArrayList<>(byName.values());
    }
}

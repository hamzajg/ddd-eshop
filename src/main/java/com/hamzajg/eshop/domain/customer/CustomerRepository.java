package com.hamzajg.eshop.domain.customer;

import java.util.List;
import java.util.Optional;

/**
 * Customer persistence contract. Implementations live in
 * `infrastructure`; the domain only knows this interface.
 */
public interface CustomerRepository {

    void save(Customer customer);

    Optional<Customer> findByName(String name);

    List<Customer> all();
}

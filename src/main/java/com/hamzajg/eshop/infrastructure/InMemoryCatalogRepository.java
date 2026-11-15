package com.hamzajg.eshop.infrastructure;

import com.hamzajg.eshop.domain.catalog.CatalogRepository;
import com.hamzajg.eshop.domain.catalog.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** In-memory catalog for tests and server-free dev runs. */
public final class InMemoryCatalogRepository implements CatalogRepository {

    private final Map<String, Product> bySku = new HashMap<>();

    public void add(Product product) {
        bySku.put(product.sku(), product);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(bySku.get(sku));
    }
}

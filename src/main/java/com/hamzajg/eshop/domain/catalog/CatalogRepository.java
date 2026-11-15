package com.hamzajg.eshop.domain.catalog;

import java.util.Optional;

/**
 * Catalog persistence contract. Implementations live in
 * `infrastructure`; the domain only knows this interface.
 */
public interface CatalogRepository {

    Optional<Product> findBySku(String sku);
}

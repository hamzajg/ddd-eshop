# ADR 0004 — First application use case and repository contracts

Date: 2026-11-15
Status: accepted

## Context

ADR 0002 deferred repositories "until the first application use case
needs them". That use case now exists: placing an order. This ADR records
the layering it introduces.

## Decisions

1. **Repository interfaces live in the domain**, implementations in
   `infrastructure` — standard DDD: `domain.catalog.CatalogRepository`,
   `domain.ordering.OrderRepository`. The domain stays framework-free;
   nothing in `domain.*` imports `infrastructure`.
2. **In-memory implementations ship with the interfaces**
   (`InMemoryCatalogRepository`, `InMemoryOrderRepository`) so tests and
   server-free runs work without persistence. A real store is a later,
   separate decision.
3. **`application.ordering.PlaceOrder`** is the first use case. It
   resolves SKUs against the catalog, snapshots product name/price into
   `OrderLine`s (reusing the ordering context's snapshot rule from ADR
   0003), persists via `OrderRepository`, and returns order number +
   total. Command/Result are records — immutable DTOs at the boundary.
4. **Use-case errors reuse domain exceptions**: unknown SKU →
   `IllegalArgumentException`, inactive product / duplicate order number →
   `IllegalStateException`. No application-specific exception hierarchy
   yet (YAGNI).
5. **Order numbers are unique** — `PlaceOrder` rejects a number already
   in the repository before doing any work.

## Consequences

- `application` and `infrastructure` packages now exist; future use cases
  follow the same shape (command in, domain calls, result out).
- The repo remains a library — `Main.java` is still a placeholder; an
  entry point (CLI or HTTP) is a separate scope.

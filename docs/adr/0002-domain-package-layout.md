# ADR 0002 — Domain package layout and first slice (catalog)

Date: 2026-11-13
Status: accepted

## Context

ADR 0001 deferred the DDD package layout until the domain model existed.
This is the first implementation slice: the product catalog.

## Decisions

1. **Layered packages under `com.hamzajg.eshop`:**
   - `domain.<context>` — aggregates, entities, value objects. Pure Java,
     no framework imports, ever.
   - `application` — use cases (added when the first one exists).
   - `infrastructure` — repository implementations (added with persistence).
   Bounded contexts get their own `domain.<context>` package so a second
   context (ordering, customers) does not collide.
2. **First context: `catalog`.** An e-shop starts with things to sell.
   - `Money` — value object (`record`): minor units + ISO currency,
     immutable, `add` refuses mixed currencies.
   - `Product` — aggregate root: SKU identity, mutable name/price through
     behaviour methods, `active` flag for delisting.
3. **No repository yet.** `CatalogRepository` arrives with the first
   application use case that needs it — interface + in-memory impl together,
   not before (YAGNI for a practice repo).
4. **Validation style:** constructors and mutators throw
   `IllegalArgumentException` for blank/invalid input and
   `NullPointerException` (via `Objects.requireNonNull`) for missing
   required objects — standard Java, no validation framework.

## Consequences

- `Main.java` stays a placeholder; the domain is a library until an
  application/infrastructure layer gives it an entry point.
- Tests live next to the domain in `src/test`, JUnit 5, no Spring context.
- Next slices (ordering, customers) reuse the same layout; persistence is
  still an open question (ADR 0001).

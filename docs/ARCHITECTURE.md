# Architecture — ddd-eshop

Current-state map. Descriptive, not aspirational; see `docs/adr/` for why.

## Current state

```
ddd-eshop/   (Gradle, single module)
├── build.gradle    java toolchain → JDK 25; junit-bom 5.9.1 + jupiter, useJUnitPlatform
├── settings.gradle rootProject
├── gradlew(bat)
└── src/main/java/com/hamzajg/Main.java   "Hello world!" placeholder
```

**Two domain slices exist**:

- `domain.catalog` — `Product` aggregate.
- `domain.ordering` — `Order` aggregate + `OrderLine` entities,
  `OrderStatus` lifecycle (PLACED → CONFIRMED → SHIPPED / CANCELLED).
- `domain.shared` — `Money` value object shared by both contexts
  (moved out of `catalog` in ADR 0003).

Layout decided in `docs/adr/0002` (packages), `0003` (ordering + shared
kernel) and `0004` (first use case + repository contracts). An
`application.ordering.PlaceOrder` use case now exists with in-memory
repositories; persistence beyond in-memory is still undecided. Still no
`application` plugin (no `run` task).

## Intended direction (not yet built)

When implemented, the e-shop should follow DDD layering:

- **Domain** — framework-free: `Product`, `Order` aggregates,
  `CatalogRepository`/`OrderRepository` interfaces, `Money` in
  `domain.shared`. `Customer`, payments and addresses are not modelled
  yet.
- **Application** — `PlaceOrder` + `ConfirmOrder`/`ShipOrder`/`CancelOrder`
  lifecycle use cases over a shared `OrderTransition` skeleton.
- **Infrastructure** — in-memory repository implementations only.
- Keep the domain pure Java — no Spring/etc. in a DDD practice repo.

The package layout and layering decisions should be recorded as an ADR in
`docs/adr/` before bulk implementation, so agents don't guess structure.

## Toolchain

Gradle runs on CI's JDK 17; `java.toolchain.languageVersion = 25` selects
the compile JDK (auto-resolved). JUnit 5 via the platform.

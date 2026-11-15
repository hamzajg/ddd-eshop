# Architecture — ddd-eshop

Current-state map. Descriptive, not aspirational; see `docs/adr/` for why.

## Current state

```
ddd-eshop/   (Gradle, single module)
├── build.gradle    java toolchain → JDK 25; junit-bom 5.9.1 + jupiter, useJUnitPlatform,
│                   application plugin → mainClass com.hamzajg.eshop.Cli
├── settings.gradle rootProject
├── gradlew(bat)
├── src/main/java/com/hamzajg/Main.java   "Hello world!" placeholder (untouched)
└── src/main/java/com/hamzajg/eshop/      domain.*, application.*, infrastructure.*, Cli
```

**Two domain slices exist**:

- `domain.catalog` — `Product` aggregate.
- `domain.ordering` — `Order` aggregate + `OrderLine` entities,
  `OrderStatus` lifecycle (PLACED → CONFIRMED → SHIPPED / CANCELLED).
- `domain.shared` — `Money` value object shared by both contexts
  (moved out of `catalog` in ADR 0003).

Layout decided in `docs/adr/0002` (packages), `0003` (ordering + shared
kernel) and `0004` (first use case + repository contracts). An
`application.ordering` use cases (`PlaceOrder` + lifecycle) exist with
in-memory repositories; persistence beyond in-memory is still undecided.
The `application` plugin is applied (`mainClass = eshop.Cli`, ADR 0005).

## Intended direction (not yet built)

When implemented, the e-shop should follow DDD layering:

- **Domain** — framework-free: `Product`, `Order` aggregates,
  `CatalogRepository`/`OrderRepository` interfaces, `Money` in
  `domain.shared`. `Customer`, payments and addresses are not modelled
  yet.
- **Application** — `PlaceOrder` + `ConfirmOrder`/`ShipOrder`/`CancelOrder`
  lifecycle use cases over a shared `OrderTransition` skeleton.
- **Infrastructure** — in-memory repository implementations only.
- **Transport** — `eshop.Cli`: REPL/`--args` shell wiring the use cases
  over in-memory repos; `gradlew run` is the demo path (ADR 0005).
- Keep the domain pure Java — no Spring/etc. in a DDD practice repo.

The package layout and layering decisions should be recorded as an ADR in
`docs/adr/` before bulk implementation, so agents don't guess structure.

## Toolchain

Gradle runs on CI's JDK 17; `java.toolchain.languageVersion = 25` selects
the compile JDK (auto-resolved). JUnit 5 via the platform.

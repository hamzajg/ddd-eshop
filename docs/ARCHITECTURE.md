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

**No domain model exists yet** — this is a scaffold for a DDD e-shop
practice project. There are no tests, no packages beyond `com.hamzajg`,
no `application` plugin (no `run` task).

## Intended direction (not yet built)

When implemented, the e-shop should follow DDD layering:

- **Domain** — framework-free: `Product`, `Order`, `Customer` aggregates,
  value objects, domain events, repository interfaces.
- **Application** — use cases orchestrating the domain.
- **Infrastructure** — repository implementations (TBD).
- Keep the domain pure Java — no Spring/etc. in a DDD practice repo.

The package layout and layering decisions should be recorded as an ADR in
`docs/adr/` before bulk implementation, so agents don't guess structure.

## Toolchain

Gradle runs on CI's JDK 17; `java.toolchain.languageVersion = 25` selects
the compile JDK (auto-resolved). JUnit 5 via the platform.

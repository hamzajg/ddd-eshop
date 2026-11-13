# ADR 0001 — Initial architecture (scaffold)

Date: 2026-11-13 (baseline recorded retroactively)
Status: accepted (describes what is built — a scaffold)

## Context

A workspace for practising DDD by building an e-shop. Currently empty:
one `Main` placeholder and a Gradle build with JUnit 5 configured.

## Decisions

1. **Gradle + plain `java` plugin.** No `application` plugin, no
   frameworks — `Main` is a placeholder, `gradlew build` compiles+tests.
   Rationale: the exercise is domain modelling, not app plumbing.
2. **JUnit 5 (Jupiter) via junit-bom 5.9.1 + `useJUnitPlatform()`.**
   Modern test stack ready for when tests arrive.
3. **JDK 25 toolchain on a JDK 17 launcher.** `java.toolchain.
   languageVersion = 25`; CI runs Gradle on 17 and lets the toolchain
   auto-resolve 25. Rationale: fleet SDK standardisation.
4. **Package root `com.hamzajg`, flat.** No subdomain packages yet —
   layout deferred until the domain model exists.
5. **Default branch `main`.** (Unlike sibling Java repos on `master`.)

## Consequences / open questions

- The DDD package layout (domain/application/infrastructure split) is
  **undecided** — first real implementation PR should propose it in an
  ADR before bulk code lands.
- Persistence choice (in-memory vs file vs DB) is open — decide with the
  first repository implementation.
- `Main.java` will likely be replaced or moved once there's a real entry
  point.

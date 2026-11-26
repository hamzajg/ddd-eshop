# AGENTS.md — ddd-eshop

Guidance for AI agents (and humans) working in this repository.

## Project

A Java/Gradle workspace for practising Domain-Driven Design by building an
e-shop. **Runnable vertical slice**: `domain.catalog` + `domain.ordering`
aggregates, `domain.shared.Money`, repository interfaces in the domain,
in-memory impls in `infrastructure`, application use cases
(`PlaceOrder`/`ListOrders` + `ConfirmOrder`/`ShipOrder`/`CancelOrder`),
`application.customer.LoginCustomer`, `domain.customer`, a CLI entry
point (`eshop.Cli`, `gradlew run`) with login/logout session, order
attribution and `orders` history. Spec-driven artifacts live in
`specs/<feature>/` (SPEC/EXAMPLES/BACKLOG/WAVES/VALIDATION + PRODUCT.md).
Decisions recorded in `docs/adr/0002`–`0005`. `Main.java` remains a
placeholder.
Stack: Java (Gradle wrapper), JUnit 5 (`junit-bom` 5.9.1), `useJUnitPlatform`.

## Commands

Verified 2026-11-13 against CI (`.github/workflows/ci.yml`) and README.

| Task | Command |
|---|---|
| Build + test | `sh gradlew build --no-daemon` (CI: JDK 25) |
| Tests only | `sh gradlew test` (30 tests) |
| Run demo | `sh gradlew run` (REPL) or `--args="catalog"` / `"place ORD-1 KB-1:2"` |

Requires: JDK 25 to run Gradle and to compile. **Compilation toolchain is
JDK 25** (`build.gradle` → `java.toolchain.languageVersion = 25`),
auto-resolved.
`application` plugin is applied with `mainClass = com.hamzajg.eshop.Cli`
(ADR 0005) — `gradlew run` works; `Main.java` stays a placeholder.

## Architecture

```
src/main/java/com/hamzajg/Main.java       placeholder entry point (untouched)
src/main/java/com/hamzajg/eshop/
  Cli.java                              REPL/args shell over the use cases
  domain/
    shared/Money.java                   money VO (add, multiply)
    catalog/{Product,CatalogRepository}.java
    ordering/{Order,OrderLine,OrderStatus,OrderRepository}.java
    customer/{Customer,CustomerRepository}.java
  application/ordering/{PlaceOrder,ListOrders,ConfirmOrder,ShipOrder,
                        CancelOrder,OrderTransition}.java   use cases
  application/customer/LoginCustomer.java
  infrastructure/InMemory*Repository.java      in-memory repo impls
build.gradle                            java toolchain 25 + application plugin
```

Layers per ADR 0002/0004/0005: `domain.<context>` (pure Java, repo
interfaces inside), `application.<context>` (use cases, command/result
records), `infrastructure` (repo impls — in-memory only), `eshop.Cli`
transport shell.

## Conventions

- DDD intent: keep the domain model pure Java (no framework deps); this is
  a practice repo, structure is the exercise.
- JUnit 5 (Jupiter) via `useJUnitPlatform()` — write tests in JUnit 5
  style.
- Gradle wrapper only (`sh gradlew`); `java` + `application` plugins —
  no other plugins without an ADR-level reason.
- KISS: don't add libraries/frameworks to a DDD practice repo — the point
  is hand-rolled domain modelling.

## Landmines

- **Default branch is `main`, not `master`.**
- The wrapper is 9.9.1 because older Gradle releases cannot run on JDK 25.
- `gradlew run` exists via the `application` plugin → `eshop.Cli`
  (ADR 0005). `Main.java` is still a placeholder — don't build on it;
  new behaviour goes in `eshop.*` packages.
- Package root is `com.hamzajg`; domain sub-packages are
  `eshop.domain.<context>` + `eshop.domain.shared` — contexts must not
  import each other's types (ADR 0003).

## Agent rules

- Branch from `main`; name branches `feat/*`, `fix/*`, `chore/*`, `docs/*`.
- Docs-only changes still go through a PR; never commit to `main` directly.
- Verify before claiming: `sh gradlew build --no-daemon` and report the
  real result.
- Keep changes minimal and consistent with surrounding code. No drive-by
  refactors.
- When the domain model lands: propose the package layout + layering in an
  ADR (`docs/adr/`) before writing bulk code.
- Update AGENTS.md / docs/ARCHITECTURE.md when structure or commands change.

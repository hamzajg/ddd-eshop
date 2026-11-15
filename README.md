# ddd-eshop

A Java/Gradle workspace for practising Domain-Driven Design by building an
e-shop.

## Current state

This repository is a **runnable e-shop MVP**: an interactive CLI
(`gradlew run`) with a seeded catalog, order placement and lifecycle
(confirm/ship/cancel), and shopper accounts — log in with a name, orders
are attributed to you, and `orders` lists your history.

```
src/main/java/com/hamzajg/eshop/Cli.java
src/main/java/com/hamzajg/eshop/domain/         bounded contexts
src/main/java/com/hamzajg/eshop/application/    use cases
src/main/java/com/hamzajg/eshop/infrastructure/ in-memory repositories
specs/                                 spec-driven feature artifacts
```

## Tech stack

- Java
- Gradle 8.2 (wrapper included)
- JUnit 5 (`junit-bom` 5.9.1)

## Building

```bash
./gradlew build
```

The `application` plugin is applied — `./gradlew run` starts the CLI.
Note the compile toolchain is JDK 25 (auto-resolved by Gradle); the Gradle
daemon itself runs on your launcher JDK.

## Tests

```bash
./gradlew test
```

## Roadmap

The intent is to grow this into a DDD e-shop: bounded contexts for catalogue,
ordering, payments, and identity, with the domain model kept independent of
framework and persistence concerns.

## License

See [LICENSE](LICENSE).

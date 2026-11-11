# ddd-eshop

A Java/Gradle workspace for practising Domain-Driven Design by building an
e-shop.

## Current state

This repository is an **early scaffold**. It currently contains a single
`Main` class and a Gradle build with JUnit 5 configured — no domain model or
e-shop features have been implemented yet.

```
src/main/java/com/hamzajg/Main.java
```

## Tech stack

- Java
- Gradle 8.2 (wrapper included)
- JUnit 5 (`junit-bom` 5.9.1)

## Building

```bash
./gradlew build
```

The `java` plugin is applied without the `application` plugin, so there is no
`run` task yet; `Main` is a placeholder entry point.

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

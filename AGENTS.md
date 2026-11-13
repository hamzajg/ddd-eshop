# AGENTS.md — ddd-eshop

Guidance for AI agents (and humans) working in this repository.

## Project

A Java/Gradle workspace for practising Domain-Driven Design by building an
e-shop. **Early scaffold**: currently a single `Main` class ("Hello world!")
+ Gradle build with JUnit 5 configured — no domain model yet. The repo is a
blank DDD canvas.
Stack: Java (Gradle wrapper), JUnit 5 (`junit-bom` 5.9.1), `useJUnitPlatform`.

## Commands

Verified 2026-11-13 against CI (`.github/workflows/ci.yml`) and README.

| Task | Command |
|---|---|
| Build + test | `sh gradlew build --no-daemon` (CI: JDK 17 launcher) |
| Tests only | `sh gradlew test` |

Requires: JDK to run Gradle (CI uses 17). **Compilation toolchain is JDK 25**
(`build.gradle` → `java.toolchain.languageVersion = 25`), auto-resolved.
**No `run` task** — the `application` plugin isn't applied; `Main` is a
placeholder.

## Architecture

```
src/main/java/com/hamzajg/Main.java     placeholder entry point
build.gradle                            java toolchain 25 + junit-bom 5.9.1
gradlew / gradle/wrapper                checked-in wrapper
```

There is no domain yet. When the e-shop model arrives it should follow DDD
conventions: domain layer framework-free (entities, value objects,
repositories as interfaces), application layer for use cases — decide and
record the package layout in an ADR before it grows.

## Conventions

- DDD intent: keep the domain model pure Java (no framework deps); this is
  a practice repo, structure is the exercise.
- JUnit 5 (Jupiter) via `useJUnitPlatform()` — write tests in JUnit 5
  style.
- Gradle wrapper only (`sh gradlew`); `java` plugin only until there's a
  reason for more.
- KISS: don't add libraries/frameworks to a DDD practice repo — the point
  is hand-rolled domain modelling.

## Landmines

- **Default branch is `main`, not `master`.**
- JDK 17 launcher / JDK 25 toolchain split — don't "fix" the numbers.
- No `run`/`application` plugin — `gradlew run` doesn't exist; don't
  document it.
- `Main.java` is a placeholder — don't build features on it; the e-shop
  domain deserves its own packages.
- Package root is `com.hamzajg` — flat, no subdomain packages yet.

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

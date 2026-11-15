# ADR 0005 — Runnable CLI entry point

Date: 2026-11-15
Status: accepted

## Context

With domain, repositories and use cases in place the repo was still a
library — `Main.java` a "Hello world!" placeholder and no `run` task.
A minimal shell makes the vertical slice exercisable end-to-end.

## Decisions

1. **Apply the `application` plugin** and set
   `mainClass = 'com.hamzajg.eshop.Cli'` → `gradlew run` now exists.
   This supersedes the AGENTS note "no run task" deliberately.
2. **`com.hamzajg.eshop.Cli` is the entry point**, not `Main.java` —
   the placeholder stays untouched; the CLI lives in the `eshop`
   package next to the layers it wires.
3. **The CLI is a thin shell**: it seeds a small demo catalog into the
   in-memory repositories, wires the use cases, and translates one
   command per line (`catalog`, `place`, `confirm`, `ship`, `cancel`,
   `show`, `quit`) into use-case calls. All business rules stay in
   domain/application — the CLI adds none.
4. **`Cli.dispatch(String)` returns the response text** so the command
   surface is unit-testable without stdin/stdout; `main` handles REPL
   vs `--args`.
5. **In-memory only** — no persistence decision is made here; swapping
   repositories later changes wiring, not commands.

## Consequences

- `gradlew run` (optionally `--args="..."`) is now the demo path;
  AGENTS.md commands table updated.
- HTTP/CLI transport remains an open question — the CLI is a stopgap,
  not the product's final interface.

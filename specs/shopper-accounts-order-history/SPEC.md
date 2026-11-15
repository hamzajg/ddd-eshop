# Spec: Shopper Accounts & Order History

> Spec-driven development artifact. This file is the contract: implementation
> is done when every acceptance criterion maps to a scenario in
> `EXAMPLES.feature` and every scenario has a passing check.

## Problem

The CLI can place and manage orders, but every order is anonymous: anyone
who knows an order number can `show` it, there is no way to ask "what are
MY orders?", and placing an order requires no identity. The shop loop is
open — a real e-shop needs a shopper who owns their orders.

## Users

- **Shopper** — a person using the CLI to browse, buy, and track orders.

## Scope (in)

- Shopper identity: `login <name>` / `logout` session in the CLI; first
  login registers the customer (find-or-create), repeat logins reuse it.
- Order attribution: every placed order records the logged-in shopper's
  name (snapshot, like `OrderLine` does for products).
- Order history: `orders` lists the logged-in shopper's orders with
  number, status and total; empty history is explicit.
- Guard rails: `place` requires a logged-in shopper; `orders` requires a
  logged-in shopper.

## Scope (out)

- Persistence across restarts (in-memory repositories stay).
- Passwords/auth — name-as-identity is the MVP contract.
- Multiple concurrent sessions, order editing, returns.
- HTTP transport — CLI remains the shell.

## Acceptance criteria

| # | Criterion | Scenario(s) |
|---|-----------|-------------|
| 1 | A shopper can log in with a name and is greeted as that shopper | Shopper logs in |
| 2 | Logging in twice with the same name reuses one customer | Repeat login reuses customer |
| 3 | An order placed while logged in records the shopper's name | Order attributed to shopper |
| 4 | `orders` lists only the logged-in shopper's orders | Order history shows own orders |
| 5 | A shopper with no orders sees an explicit empty history | Empty order history |
| 6 | `place` and `orders` fail clearly when nobody is logged in | Placing an order requires login; Orders command requires login |

## Decisions

- ADR-style: customer context lands as `domain.customer` with a
  name-keyed repository; `Order` snapshots `customerName` (immutable
  string) rather than importing `Customer` — same rule as `OrderLine`
  snapshots `Product` fields (ADR 0003).
- Identity is the name string for MVP; no credentials, no `CustomerId`
  value object yet. Recorded here so a future auth feature can revisit.

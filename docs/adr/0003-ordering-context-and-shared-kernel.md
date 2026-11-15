# ADR 0003 — Ordering context and the shared kernel

Date: 2026-11-15
Status: accepted

## Context

ADR 0002 established `domain.<context>` packages with `catalog` as the
first slice. The e-shop's second bounded context is ordering: a customer
places an order for catalog products. Money is needed by both contexts.

## Decisions

1. **`Money` moves to `domain.shared`.** It was introduced inside
   `catalog`, but it is not catalog-specific — ordering totals need it
   too. A shared-kernel package is the DDD answer for types every
   context uses; duplicating it would be worse. `Product` now imports it
   from `domain.shared` — contexts may depend on the shared kernel, never
   on each other's domain types.
2. **Ordering does not import catalog types.** `OrderLine` snapshots the
   SKU, description and unit price at order time instead of holding a
   `Product` reference. This keeps the contexts decoupled: a later
   catalog price change cannot rewrite order history, and the ordering
   context stays usable if the catalog is replaced.
3. **`Order` is the aggregate root; `OrderLine` is an entity owned by
   it** (identity only meaningful inside the order). Lines are immutable
   snapshots; quantity changes are a future slice if needed.
4. **Currency consistency is an order invariant**: all lines must share
   one currency or `Order.place` rejects them — a mixed-currency total
   would be meaningless.
5. **Lifecycle is explicit**: `PLACED → CONFIRMED → SHIPPED`, with
   `CANCELLED` reachable from `PLACED`/`CONFIRMED`. Invalid transitions
   throw `IllegalStateException`; terminal states are `SHIPPED` and
   `CANCELLED`. Payment, shipping addresses and customer identity are
   deliberately out of scope.
6. **No repository yet** — same rule as ADR 0002: interfaces arrive with
   the first application use case that needs them.

## Consequences

- `domain.shared` now exists for cross-context types; keep it tiny —
  only genuinely shared kernel types belong there.
- `Money.multiply(int)` was added for line totals; factor must be > 0.
- Tests mirror the catalog slice's style: pure JUnit 5, no framework.

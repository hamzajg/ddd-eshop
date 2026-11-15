# Validation — shopper-accounts-order-history

## Traceability

| AC | Criterion | Scenarios | Wave(s) | Test evidence |
|----|-----------|-----------|---------|---------------|
| 1 | A shopper can log in with a name and is greeted as that shop | 0 | — | — |
| 2 | Logging in twice with the same name reuses one customer | 0 | — | — |
| 3 | An order placed while logged in records the shopper's name | 0 | — | — |
| 4 | `orders` lists only the logged-in shopper's orders | 0 | — | — |
| 5 | A shopper with no orders sees an explicit empty history | 0 | — | — |
| 6 | `place` and `orders` fail clearly when nobody is logged in | 0 | — | — |

## Scenario coverage

- Scenarios: 7 total, 0 with detected test evidence, 7 without
- ✗ `Shopper logs in` (@w1)
- ✗ `Repeat login reuses customer` (@w1)
- ✗ `Order attributed to shopper` (@w2)
- ✗ `Placing an order requires login` (@w2)
- ✗ `Order history shows own orders` (@w3)
- ✗ `Orders command requires login` (@w3)
- ✗ `Empty order history` (@w3)

## Demo script (manual walkthrough)

1. **Shopper logs in** — run the app, perform the scenario, observe the outcome.
2. **Order attributed to shopper** — run the app, perform the scenario, observe the outcome.
3. **Order history shows own orders** — run the app, perform the scenario, observe the outcome.

## Gaps

- 7 scenario(s) lack detected test evidence — verify manually or add checks

Note: 'no evidence' means the scenario name was not found in
test files — verify by hand before treating it as a real gap.
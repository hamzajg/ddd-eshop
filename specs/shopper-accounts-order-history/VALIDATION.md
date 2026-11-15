# Validation — shopper-accounts-order-history

## Traceability

| AC | Criterion | Scenarios | Wave(s) | Test evidence |
|----|-----------|-----------|---------|---------------|
| 1 | A shopper can log in with a name and is greeted as that shop | 2 | @w1 | CliTest.java |
| 2 | Logging in twice with the same name reuses one customer | 2 | @w1 | CliTest.java |
| 3 | An order placed while logged in records the shopper's name | 2 | @w2 | CliTest.java, PlaceOrderTest.java |
| 4 | `orders` lists only the logged-in shopper's orders | 3 | @w3 | CliTest.java |
| 5 | A shopper with no orders sees an explicit empty history | 3 | @w3 | CliTest.java |
| 6 | `place` and `orders` fail clearly when nobody is logged in | 5 | @w2, @w3 | CliTest.java, PlaceOrderTest.java |

## Scenario coverage

- Scenarios: 7 total, 7 with detected test evidence, 0 without
- ✓ `Shopper logs in` (@w1)
- ✓ `Repeat login reuses customer` (@w1)
- ✓ `Order attributed to shopper` (@w2)
- ✓ `Placing an order requires login` (@w2)
- ✓ `Order history shows own orders` (@w3)
- ✓ `Orders command requires login` (@w3)
- ✓ `Empty order history` (@w3)

## Demo script (manual walkthrough)

1. **Shopper logs in** — run the app, perform the scenario, observe the outcome.
2. **Order attributed to shopper** — run the app, perform the scenario, observe the outcome.
3. **Order history shows own orders** — run the app, perform the scenario, observe the outcome.

## Gaps

- none — spec is fully traced

Note: 'no evidence' means neither the scenario name nor a
token match was found in test files — verify by hand before
treating it as a real gap. '~' marks weak token-match evidence.
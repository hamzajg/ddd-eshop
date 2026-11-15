# Waves — shopper-accounts-order-history

Each wave IS an iteration: a time-boxed, independently shippable
increment — one branch, one PR, green tests, demoable outcome.
Do not start wave N+1 until wave N merges.

## Wave 1 — iteration 1

Scenarios proven by this wave:
- Shopper logs in
- Repeat login reuses customer

| Item | Title | Size | Deps | Notes |
|------|-------|------|------|-------|
| US1 | As a shopper, I want to log in with my name, so that the shop knows who I am | M | — | customer context (domain.customer), find-or-create on login, login/logout CLI commands, session state in Cli. |

## Wave 2 — iteration 2

Scenarios proven by this wave:
- Order attributed to shopper
- Placing an order requires login

| Item | Title | Size | Deps | Notes |
|------|-------|------|------|-------|
| US2 | As a shopper, I want my orders attributed to me, so that they belong to me | M | US1 | Order.customerName snapshot via Order.place param; PlaceOrder.Command gains customerName; place requires login. |

## Wave 3 — iteration 3

Scenarios proven by this wave:
- Order history shows own orders
- Orders command requires login
- Empty order history

| Item | Title | Size | Deps | Notes |
|------|-------|------|------|-------|
| US3 | As a shopper, I want to see my order history, so that I can track my purchases | S | US2 | OrderRepository.findAllByCustomer + ListOrders use case + `orders` CLI command. |

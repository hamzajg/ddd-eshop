# Backlog: Shopper Accounts & Order History

Items are USER STORIES in the parseable format below — keep the heading
shape `## US<n> — <story>` and the field lines intact so `wave_plan` can
parse them. Write the story as: "As a <role>, I want <goal>, so that
<value>" — the title after the dash IS the story.

```
## US<n> — As a <role>, I want <goal>, so that <value>
- Scenarios: @w1, @w2
- Depends: US1, US3        (optional; omit for wave-1 items)
- Size: S|M|L
- Notes: <one line>
```

Order stories by user value; dependencies express technical order only.

## US1 — As a shopper, I want to log in with my name, so that the shop knows who I am
- Scenarios: @w1
- Size: M
- Notes: customer context (domain.customer), find-or-create on login, login/logout CLI commands, session state in Cli.

## US2 — As a shopper, I want my orders attributed to me, so that they belong to me
- Scenarios: @w2
- Depends: US1
- Size: M
- Notes: Order.customerName snapshot via Order.place param; PlaceOrder.Command gains customerName; place requires login.

## US3 — As a shopper, I want to see my order history, so that I can track my purchases
- Scenarios: @w3
- Depends: US2
- Size: S
- Notes: OrderRepository.findAllByCustomer + ListOrders use case + `orders` CLI command.

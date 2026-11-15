# Shopper Accounts & Order History — specifications by example
# Scenario tags @w<N> map items to delivery waves (see WAVES.md / BACKLOG.md).

Feature: Shopper Accounts & Order History

  @w1 @happy-path
  Scenario: Shopper logs in
    Given the shop has no session
    When the shopper runs "login alice"
    Then the session greets "alice" as a shopper
    And a customer named "alice" exists in the shop

  @w1
  Scenario: Repeat login reuses customer
    Given the shopper "alice" has logged in before
    When the shopper runs "login alice" again
    Then the same customer "alice" is reused, not duplicated

  @w2 @happy-path
  Scenario: Order attributed to shopper
    Given the shopper "alice" is logged in
    When she places order "1001" with "KB-1:1"
    Then order "1001" records customer "alice"

  @w2
  Scenario: Placing an order requires login
    Given no shopper is logged in
    When she runs "place 1002 KB-1:1"
    Then she gets a clear "please log in" error

  @w3 @happy-path
  Scenario: Order history shows own orders
    Given "alice" placed order "1001" and "bob" placed order "2001"
    When "alice" is logged in and runs "orders"
    Then she sees order "1001" with its status and total
    And she does not see order "2001"

  @w3
  Scenario: Orders command requires login
    Given no shopper is logged in
    When she runs "orders"
    Then she gets a clear "please log in" error

  @w3
  Scenario: Empty order history
    Given "carol" is logged in and has placed no orders
    When she runs "orders"
    Then she sees an explicit "no orders yet" message

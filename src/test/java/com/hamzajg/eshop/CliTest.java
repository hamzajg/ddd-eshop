package com.hamzajg.eshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CliTest {

    private final Cli cli = new Cli();

    @Test
    void catalogListsSeededProducts() {
        String out = cli.dispatch("catalog");
        assertTrue(out.contains("KB-1"));
        assertTrue(out.contains("Mechanical Keyboard"));
        assertTrue(out.contains("MON-27"));
    }

    @Test
    void fullOrderLifecycleThroughCommands() {
        cli.dispatch("login alice");
        assertTrue(cli.dispatch("place ORD-1 KB-1:2 MS-1:1")
                .contains("total 119.97"));
        assertEquals("CONFIRMED ORD-1", cli.dispatch("confirm ORD-1"));
        assertEquals("SHIPPED ORD-1", cli.dispatch("ship ORD-1"));

        String shown = cli.dispatch("show ORD-1");
        assertTrue(shown.contains("[SHIPPED]"));
        assertTrue(shown.contains("total 119.97"));
    }

    @Test
    void loginGreetsShopperAndLogoutClearsSession() {
        // covers scenarios: Shopper logs in; Repeat login reuses customer
        String first = cli.dispatch("login alice");
        assertTrue(first.contains("alice"));
        assertTrue(cli.dispatch("login alice").contains("back"));
        assertTrue(cli.dispatch("logout").contains("logged out"));
        assertTrue(cli.dispatch("login bob").contains("bob"));
    }

    @Test
    void ordersListsOnlyOwnOrders() {
        // covers scenario: Order history shows own orders
        cli.dispatch("login alice");
        cli.dispatch("place ORD-A KB-1:1");
        cli.dispatch("login bob");
        cli.dispatch("place ORD-B MS-1:1");
        cli.dispatch("login alice");
        String out = cli.dispatch("orders");
        assertTrue(out.contains("ORD-A"));
        assertFalse(out.contains("ORD-B"));
        assertTrue(out.contains("[PLACED]"));
    }

    @Test
    void ordersEmptyAndRequiresLogin() {
        // covers scenarios: Orders command requires login; Empty order history
        cli.dispatch("login carol");
        assertTrue(cli.dispatch("orders").contains("no orders"));
        Cli fresh = new Cli();
        String out = fresh.dispatch("orders");
        assertTrue(out.startsWith("error:"));
        assertTrue(out.contains("log in"));
    }

    @Test
    void placingRequiresLogin() {
        // covers scenario: Placing an order requires login
        String out = cli.dispatch("place ORD-9 KB-1:1");
        assertTrue(out.startsWith("error:"));
        assertTrue(out.contains("log in"));
    }

    @Test
    void badCommandsReturnErrorsNotExceptions() {
        assertTrue(cli.dispatch("place ORD-2 NOPE:1").startsWith("error:"));
        assertTrue(cli.dispatch("show NOPE").startsWith("error:"));
        assertTrue(cli.dispatch("bogus").contains("unknown command"));
        assertEquals("", cli.dispatch("   "));
        assertNull(cli.dispatch("quit"));
    }
}

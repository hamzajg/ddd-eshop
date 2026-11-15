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
        assertTrue(cli.dispatch("place ORD-1 KB-1:2 MS-1:1")
                .contains("total 119.97"));
        assertEquals("CONFIRMED ORD-1", cli.dispatch("confirm ORD-1"));
        assertEquals("SHIPPED ORD-1", cli.dispatch("ship ORD-1"));

        String shown = cli.dispatch("show ORD-1");
        assertTrue(shown.contains("[SHIPPED]"));
        assertTrue(shown.contains("total 119.97"));
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

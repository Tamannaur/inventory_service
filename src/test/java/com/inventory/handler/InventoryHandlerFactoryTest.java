package com.inventory.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryHandlerFactoryTest {

    @Autowired
    private InventoryHandlerFactory factory;

    @Test
    void testGetHandler_ExpiryType() {
        InventoryHandler handler = factory.getHandler("EXPIRY");

        assertNotNull(handler);
        assertTrue(handler instanceof ExpiryBasedInventoryHandler);
    }

    @Test
    void testGetHandler_InvalidType() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> factory.getHandler("UNKNOWN"));

        assertEquals("Unknown handler type: UNKNOWN", exception.getMessage());
    }
}



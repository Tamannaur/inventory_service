package com.inventory.handler;

import org.springframework.stereotype.Component;

@Component
public class InventoryHandlerFactory {

    public InventoryHandler getHandler(String type) {
        if ("EXPIRY".equalsIgnoreCase(type)) {
            return new ExpiryBasedInventoryHandler();
        }
        throw new IllegalArgumentException("Unknown handler type: " + type);
    }
}


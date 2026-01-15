package com.inventory.handler;

import com.inventory.entity.Inventory;
import com.inventory.repository.InventoryRepository;

import java.util.List;

public class ExpiryBasedInventoryHandler implements InventoryHandler {
    @Override
    public List<Inventory> handleInventory(Long productId, InventoryRepository repository) {
        return repository.findByProductIdOrderByExpiryDateAsc(productId);
    }
}


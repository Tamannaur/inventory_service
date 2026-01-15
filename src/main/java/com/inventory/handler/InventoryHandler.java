package com.inventory.handler;
import com.inventory.entity.Inventory;
import com.inventory.repository.InventoryRepository;

import java.util.List;

public interface InventoryHandler {
    List<Inventory> handleInventory(Long productId, InventoryRepository repository);
}


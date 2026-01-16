package com.inventory.service;
import com.inventory.dto.InventoryResponse;
import com.inventory.entity.Inventory;
import java.util.List;

public interface InventoryService {
    InventoryResponse getInventoryByProduct(Long productId);
    InventoryResponse updateInventory(Long productId, int quantity, boolean fromOutSide);
}


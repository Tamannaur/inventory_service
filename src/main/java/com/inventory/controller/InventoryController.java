package com.inventory.controller;
import com.inventory.dto.InventoryResponse;
import com.inventory.entity.Inventory;
import com.inventory.entity.InventoryUpdateReq;
import com.inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId) {
        InventoryResponse response = service.getInventoryByProduct(productId);
        if (null == response){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<InventoryResponse> updateInventory(@RequestBody InventoryUpdateReq inventoryUpdateReq,@RequestHeader boolean fromOutSide) {
        InventoryResponse response = service.updateInventory(inventoryUpdateReq.getProductId(), inventoryUpdateReq.getQuantity(), fromOutSide);
        if (null == response){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
    }
}



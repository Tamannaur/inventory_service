package com.inventory.service;

import com.inventory.dto.InventoryResponse;
import com.inventory.entity.Inventory;
import com.inventory.handler.InventoryHandler;
import com.inventory.handler.InventoryHandlerFactory;
import com.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final InventoryHandlerFactory factory;

    public InventoryServiceImpl(InventoryRepository repository, InventoryHandlerFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    @Override
    public InventoryResponse getInventoryByProduct(Long productId) {
        InventoryHandler handler = factory.getHandler("EXPIRY");
        List<Inventory> batches = handler.handleInventory(productId, repository);

        if (batches.isEmpty()) {
            throw new RuntimeException("Batch is empty");
        }

        InventoryResponse response = new InventoryResponse();
        response.setProductId(productId);
        response.setProductName(batches.get(0).getProductName());

        List<InventoryResponse.BatchDto> batchDtos = batches.stream()
                .map(b -> {
                    InventoryResponse.BatchDto dto = new InventoryResponse.BatchDto();
                    dto.setBatchId(b.getBatchId());
                    dto.setQuantity(b.getQuantity());
                    dto.setExpiryDate(b.getExpiryDate().toString());
                    return dto;
                })
                .toList();

        response.setBatches(batchDtos);
        return response;
    }


    @Override
    public InventoryResponse updateInventory(Long productId, int quantity) {
        List<Inventory> batches = repository.findByProductIdOrderByExpiryDateAsc(productId);

        if (batches.isEmpty()) {
            throw new RuntimeException("Batch is Empty");
        }

        int remaining = quantity;
        for (Inventory batch : batches) {
            if (remaining <= 0) break;
            batch.setQuantity(quantity);
            repository.save(batch);
        }

        InventoryResponse response = new InventoryResponse();
        response.setProductId(productId);
        response.setProductName(batches.get(0).getProductName());

        List<InventoryResponse.BatchDto> batchDtos = batches.stream()
                .map(b -> {
                    InventoryResponse.BatchDto dto = new InventoryResponse.BatchDto();
                    dto.setBatchId(b.getBatchId());
                    dto.setQuantity(b.getQuantity());
                    dto.setExpiryDate(b.getExpiryDate().toString());
                    return dto;
                })
                .toList();

        response.setBatches(batchDtos);
        return response;
    }

}


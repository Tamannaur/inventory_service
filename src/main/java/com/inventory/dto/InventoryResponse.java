package com.inventory.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long productId;
    private String productName;
    private List<BatchDto> batches;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchDto {
        private Long batchId;
        private Integer quantity;
        private String expiryDate;

    }
}

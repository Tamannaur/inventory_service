package com.inventory.controller;

import com.inventory.dto.InventoryResponse;
import com.inventory.entity.InventoryUpdateReq;
import com.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InventoryControllerTest {

    @Mock
    private InventoryService service;

    @InjectMocks
    private InventoryController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInventory_Found() {
        Long productId = 1003L;
        InventoryResponse mockResponse = new InventoryResponse();
        InventoryResponse.BatchDto batchDto = new InventoryResponse.BatchDto();
        batchDto.setQuantity(10);
        mockResponse.setProductId(productId);
        mockResponse.setBatches(List.of(batchDto));

        when(service.getInventoryByProduct(productId)).thenReturn(mockResponse);

        ResponseEntity<InventoryResponse> response = controller.getInventory(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(service, times(1)).getInventoryByProduct(productId);
    }

    @Test
    void testGetInventory_NotFound() {
        Long productId = 1003L;
        when(service.getInventoryByProduct(productId)).thenReturn(null);

        ResponseEntity<InventoryResponse> response = controller.getInventory(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).getInventoryByProduct(productId);
    }


    @Test
    void testUpdateInventory_Found() {
        InventoryUpdateReq req = new InventoryUpdateReq();
        req.setProductId(1003L);
        req.setQuantity(5);

        boolean fromOutSide = true;

        InventoryResponse mockResponse = new InventoryResponse();
        InventoryResponse.BatchDto batchDto = new InventoryResponse.BatchDto();
        batchDto.setQuantity(15);
        mockResponse.setProductId(1003L);
        mockResponse.setBatches(List.of(batchDto));

        when(service.updateInventory(req.getProductId(), req.getQuantity(), fromOutSide))
                .thenReturn(mockResponse);

        ResponseEntity<InventoryResponse> response = controller.updateInventory(req, fromOutSide);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(service, times(1)).updateInventory(req.getProductId(), req.getQuantity(), fromOutSide);
    }

    @Test
    void testUpdateInventory_NotFound() {
        InventoryUpdateReq req = new InventoryUpdateReq();
        req.setProductId(1003L);
        req.setQuantity(5);

        boolean fromOutSide = false;

        when(service.updateInventory(req.getProductId(), req.getQuantity(), fromOutSide))
                .thenReturn(null);

        ResponseEntity<InventoryResponse> response = controller.updateInventory(req, fromOutSide);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).updateInventory(req.getProductId(), req.getQuantity(), fromOutSide);
    }
}

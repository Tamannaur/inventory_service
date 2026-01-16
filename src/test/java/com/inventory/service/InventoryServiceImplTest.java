package com.inventory.service;

import com.inventory.dto.InventoryResponse;
import com.inventory.entity.Inventory;
import com.inventory.handler.InventoryHandler;
import com.inventory.handler.InventoryHandlerFactory;
import com.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

    @Mock
    private InventoryRepository repository;

    @Mock
    private InventoryHandlerFactory factory;

    @Mock
    private InventoryHandler handler;

    @InjectMocks
    private InventoryServiceImpl service;


    @Test
    void testGetInventoryByProduct_Found() {
        Long productId = 1L;

        Inventory inv = new Inventory();
        inv.setBatchId(101L);
        inv.setProductId(productId);
        inv.setProductName("TestProduct");
        inv.setQuantity(10);
        inv.setExpiryDate(LocalDate.of(2026, 1, 1));

        when(factory.getHandler("EXPIRY")).thenReturn(handler);
        when(handler.handleInventory(productId, repository)).thenReturn(List.of(inv));

        InventoryResponse response = service.getInventoryByProduct(productId);

        assertNotNull(response);
        assertEquals(productId, response.getProductId());
        assertEquals("TestProduct", response.getProductName());
        assertEquals(1, response.getBatches().size());
        assertEquals(101L, response.getBatches().get(0).getBatchId());

        verify(factory).getHandler("EXPIRY");
        verify(handler).handleInventory(productId, repository);
    }

    @Test
    void testGetInventoryByProduct_EmptyListThrowsException() {
        Long productId = 2L;

        when(factory.getHandler("EXPIRY")).thenReturn(handler);
        when(handler.handleInventory(productId, repository)).thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getInventoryByProduct(productId));

        assertEquals("Batch is empty", ex.getMessage());
    }


    @Test
    void testUpdateInventory_FromOutside() {
        Long productId = 1L;

        Inventory inv = new Inventory();
        inv.setBatchId(201L);
        inv.setProductId(productId);
        inv.setProductName("TestProduct");
        inv.setQuantity(10);
        inv.setExpiryDate(LocalDate.of(2026, 2, 1));

        when(repository.findByProductIdOrderByExpiryDateAsc(productId)).thenReturn(List.of(inv));
        when(repository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryResponse response = service.updateInventory(productId, 5, true);

        assertNotNull(response);
        assertEquals(productId, response.getProductId());
        assertEquals("TestProduct", response.getProductName());
        assertEquals(1, response.getBatches().size());
        verify(repository).findByProductIdOrderByExpiryDateAsc(productId);
        verify(repository).save(any(Inventory.class));
    }

    @Test
    void testUpdateInventory_NotFromOutside() {
        Long productId = 1L;

        Inventory inv = new Inventory();
        inv.setBatchId(301L);
        inv.setProductId(productId);
        inv.setProductName("TestProduct");
        inv.setQuantity(10);
        inv.setExpiryDate(LocalDate.of(2026, 3, 1));

        when(repository.findByProductIdOrderByExpiryDateAsc(productId)).thenReturn(List.of(inv));
        when(repository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryResponse response = service.updateInventory(productId, 20, false);

        assertNotNull(response);
        assertEquals(productId, response.getProductId());
        assertEquals("TestProduct", response.getProductName());
        assertEquals(20, response.getBatches().get(0).getQuantity());

        verify(repository).findByProductIdOrderByExpiryDateAsc(productId);
        verify(repository).save(any(Inventory.class));
    }

    @Test
    void testUpdateInventory_EmptyListThrowsException() {
        Long productId = 99L;

        when(repository.findByProductIdOrderByExpiryDateAsc(productId)).thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateInventory(productId, 5, true));

        assertEquals("Batch is Empty", ex.getMessage());
    }
}

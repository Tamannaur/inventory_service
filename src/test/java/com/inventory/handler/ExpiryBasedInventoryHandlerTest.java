package com.inventory.handler;

import com.inventory.entity.Inventory;
import com.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExpiryBasedInventoryHandlerTest {

    @Mock
    private InventoryRepository repository;

    @InjectMocks
    private ExpiryBasedInventoryHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleInventory_ReturnsSortedList() {
        Long productId = 1003L;

        Inventory inv1 = new Inventory();
        inv1.setProductId(productId);
        inv1.setExpiryDate(LocalDate.of(2026, 1, 1));

        Inventory inv2 = new Inventory();
        inv2.setProductId(productId);
        inv2.setExpiryDate(LocalDate.of(2026, 2, 1));

        List<Inventory> mockList = Arrays.asList(inv1, inv2);

        when(repository.findByProductIdOrderByExpiryDateAsc(productId)).thenReturn(mockList);

        List<Inventory> result = handler.handleInventory(productId, repository);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(inv1, result.get(0));
        assertEquals(inv2, result.get(1));

        verify(repository, times(1)).findByProductIdOrderByExpiryDateAsc(productId);
    }

    @Test
    void testHandleInventory_EmptyList() {
        Long productId = 1003L;

        when(repository.findByProductIdOrderByExpiryDateAsc(productId)).thenReturn(List.of());

        List<Inventory> result = handler.handleInventory(productId, repository);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository, times(1)).findByProductIdOrderByExpiryDateAsc(productId);
    }
}


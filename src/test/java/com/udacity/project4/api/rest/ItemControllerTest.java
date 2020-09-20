package com.udacity.project4.api.rest;

import com.udacity.project4.domain.model.Item;
import com.udacity.project4.domain.model.User;
import com.udacity.project4.domain.repository.ItemRepository;
import com.udacity.project4.infrastructure.exception.handler.ResourceNotFoundException;
import com.udacity.project4.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @BeforeEach
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsReturnAllItemsSuccessfully() {
        when(itemRepository.findAll()).thenReturn(this.createItems());

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void getItemByIdIsSuccessful() throws ResourceNotFoundException {
        when(itemRepository.findById(0L)).thenReturn(Optional.of(this.createItem()));
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item 01", response.getBody().getName());
    }

    @Test
    public void getItemByIdThrowsErrorWhenInvalidIdSupplied() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<Item> response = itemController.getItemById(200L);
        });
        String expectedMessage = "Item not found for the provided id :: 200";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getItemByNameIsSuccessful() throws ResourceNotFoundException {
        when(itemRepository.findByName("Item")).thenReturn(this.createItems());
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Item 01", response.getBody().get(0).getName());
    }

    @Test
    public void getItemByNameThrowsErrorWhenInvalidNameSupplied() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<List<Item>> response = itemController.getItemsByName("NonExistingItem");
        });
        String expectedMessage = "Item not found for the provided name :: NonExistingItem";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(0L);
        item.setName("Item 01");
        item.setPrice(new BigDecimal("1.00"));
        item.setDescription("Lorem ipsum dolor 01");
        return item;
    }

    private List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(0L);
        item1.setName("Item 01");
        item1.setPrice(new BigDecimal("1.00"));
        item1.setDescription("Lorem ipsum dolor 01");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Item 02");
        item2.setPrice(new BigDecimal("2.00"));
        item2.setDescription("Lorem ipsum dolor 02");

        items.add(item1);
        items.add(item2);
        return items;
    }

}
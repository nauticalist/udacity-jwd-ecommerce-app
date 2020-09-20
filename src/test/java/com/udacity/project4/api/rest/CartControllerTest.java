package com.udacity.project4.api.rest;

import com.udacity.project4.api.model.ModifyCartRequest;
import com.udacity.project4.domain.model.Cart;
import com.udacity.project4.domain.model.Item;
import com.udacity.project4.domain.model.User;
import com.udacity.project4.domain.model.UserOrder;
import com.udacity.project4.domain.repository.CartRepository;
import com.udacity.project4.domain.repository.ItemRepository;
import com.udacity.project4.domain.repository.UserRepository;
import com.udacity.project4.infrastructure.exception.handler.ResourceNotFoundException;
import com.udacity.project4.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartControllerTest {
    private CartController cartController;

    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private User user;
    private Item item1;

    @BeforeEach
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        user = new User();
        user.setId(0L);
        user.setUsername("testuser");
        user.setPassword("testpassword");

        item1 = new Item();
        item1.setId(0L);
        item1.setName("Item 01");
        item1.setPrice(new BigDecimal("1.00"));
        item1.setDescription("Lorem ipsum dolor 01");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Item 02");
        item2.setPrice(new BigDecimal("2.00"));
        item2.setDescription("Lorem ipsum dolor 02");


        Cart cart = new Cart();
        cart.setId(0L);
        cart.addItem(item1);
        cart.addItem(item2);
        cart.setUser(user);

        user.setCart(cart);

        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.ofNullable(user));
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(item1));
    }

    @Test
    public void addToCartIsSuccessful() throws ResourceNotFoundException {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testuser");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addToCartFailsWhenItemIdNotFounds() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(200L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testuser");

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        });
        String expectedMessage = "Item not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void addToCartFailsWhenUserNotFounds() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("ghost");

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        });
        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void removeFromCartIsSuccessful() throws ResourceNotFoundException {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testuser");

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void removeFromCartFailsWhenItemIdNotFounds() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(200L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testuser");

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        });
        String expectedMessage = "Item not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void removeFromCartFailsWhenUserNotFounds() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("ghost");

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        });
        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
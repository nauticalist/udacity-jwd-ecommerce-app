package com.udacity.project4.api.rest;

import com.udacity.project4.domain.model.Cart;
import com.udacity.project4.domain.model.Item;
import com.udacity.project4.domain.model.User;
import com.udacity.project4.domain.model.UserOrder;
import com.udacity.project4.domain.repository.OrderRepository;
import com.udacity.project4.domain.repository.UserRepository;
import com.udacity.project4.infrastructure.exception.handler.ResourceNotFoundException;
import com.udacity.project4.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderControllerTest {
    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }


    @Test
    public void submitOrderIsSuccessful() throws ResourceNotFoundException {
        User user = createUserWithCart();
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        UserOrder order = response.getBody();
        assertNotNull(response);
        assertNotNull(order);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals((3), order.getTotal().intValue());

    }

    @Test
    public void submitOrderFailsForNonExistingUser(){
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<UserOrder> response = orderController.submit("ghost");
        });
        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getOrderForUserIsSuccessful() throws ResourceNotFoundException {
        User user = createUserWithCart();
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));

        ResponseEntity<UserOrder> orderResponse = orderController.submit(user.getUsername());
        List<UserOrder> orders = new ArrayList<>();
        orders.add(orderResponse.getBody());
        when(orderRepository.findByUser(user)).thenReturn(orders);
        orderController.submit(user.getUsername());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response);
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void getOrderFailsForNonExistingUser(){
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("ghost");
        });
        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private User createUserWithCart() {
        User user = new User();
        user.setId(0L);
        user.setUsername("testuser");
        user.setPassword("testpassword");

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


        Cart cart = new Cart();
        cart.setId(0L);
        cart.addItem(item1);
        cart.addItem(item2);
        cart.setUser(user);

        user.setCart(cart);

        return user;
    }

}
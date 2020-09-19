package com.udacity.project4.api.rest;

import com.udacity.project4.domain.model.User;
import com.udacity.project4.domain.model.UserOrder;
import com.udacity.project4.domain.repository.OrderRepository;
import com.udacity.project4.domain.repository.UserRepository;
import com.udacity.project4.infrastructure.exception.handler.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final static Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;


    @PostMapping("/submit/{username}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserOrder> submit(@PathVariable String username)  throws ResourceNotFoundException{
        User user = getUser(username);
        LOG.info("[ORDER] [CREATE] Create new order for user initiated:" + username);

        UserOrder order = UserOrder.createFromCart(user.getCart());
        UserOrder orderSaved = orderRepository.save(order);

        LOG.info("[ORDER] [CREATE] Create new order for user completed:" + orderSaved);

        return new ResponseEntity<>(orderSaved, HttpStatus.CREATED);

    }

    @GetMapping("/history/{username}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) throws ResourceNotFoundException {
        User user = getUser(username);

        LOG.info("[ORDER] [LIST] List orders for user:" + username);

        List<UserOrder> orders = orderRepository.findByUser(user);
        if (orders == null || orders.isEmpty()) {
            LOG.info("[ORDER] [LIST] No orders found for user:" + username);
            throw new ResourceNotFoundException(String.format("No orders found for the provided name :: %s", username));
        }
        LOG.info("[ORDER] [LIST] Orders found for user:" + orders.toString());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    private User getUser(String username) throws ResourceNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
package com.udacity.project4.api.rest;

import com.udacity.project4.api.model.ModifyCartRequest;
import com.udacity.project4.domain.model.Cart;
import com.udacity.project4.domain.model.Item;
import com.udacity.project4.domain.model.User;
import com.udacity.project4.domain.repository.CartRepository;
import com.udacity.project4.domain.repository.ItemRepository;
import com.udacity.project4.domain.repository.UserRepository;
import com.udacity.project4.infrastructure.exception.handler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/addToCart")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) throws ResourceNotFoundException {
        User user = this.getUser(request.getUsername());
        Item item = this.getItem(request.getItemId());

        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.addItem(item));
        cartRepository.save(cart);

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) throws ResourceNotFoundException {
        User user = this.getUser(request.getUsername());
        Item item = this.getItem(request.getItemId());

        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.removeItem(item));
        cartRepository.save(cart);

        return ResponseEntity.ok(cart);
    }

    private User getUser(String username) throws ResourceNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Item getItem(Long id) throws ResourceNotFoundException{
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

}
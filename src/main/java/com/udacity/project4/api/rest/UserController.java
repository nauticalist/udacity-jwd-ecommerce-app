package com.udacity.project4.api.rest;

import com.udacity.project4.api.model.CreateUserRequest;
import com.udacity.project4.domain.model.Cart;
import com.udacity.project4.domain.model.User;
import com.udacity.project4.domain.repository.CartRepository;
import com.udacity.project4.domain.repository.UserRepository;
import com.udacity.project4.infrastructure.exception.handler.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/id/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<User> findById(@PathVariable Long id) throws ResourceNotFoundException{
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        LOG.info("[USER] [GET] User found by userId: " + user.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping("/{username}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<User> findByUserName(@PathVariable String username) throws ResourceNotFoundException{
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        LOG.info("[USER] [GET] User found by username: " + user.getUsername());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = new User();
        Cart cart = new Cart();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        user.setCart(cart);
        cartRepository.save(cart);
        userRepository.save(user);

        LOG.info("[USER] [CREATE] User created: " + user.getUsername());

        return new ResponseEntity<>(user, HttpStatus.CREATED) ;
    }
}

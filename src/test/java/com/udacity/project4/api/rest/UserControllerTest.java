package com.udacity.project4.api.rest;

import com.udacity.project4.api.model.CreateUserRequest;
import com.udacity.project4.domain.model.User;
import com.udacity.project4.domain.repository.CartRepository;
import com.udacity.project4.domain.repository.UserRepository;
import com.udacity.project4.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private User user;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);


    }

    @Test
    public void createUserIsSuccessfull() {
        when(encoder.encode("testPassw0rd!")).thenReturn("thisIsHashed");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testPassw0rd!");
        userRequest.setConfirmPassword("testPassw0rd!");

        ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User userResponse = response.getBody();
        assertNotNull(userResponse);
        assertEquals(0, userResponse.getId());
        assertEquals("testuser", userResponse.getUsername());
        assertEquals("thisIsHashed", userResponse.getPassword());
    }

    @Test
    public void createUserFailsWithDifferentPassword() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testPassw0rd!d");
        userRequest.setConfirmPassword("testPassw0rd!xc");

        ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    private User createUser() {
        user = new User();
        user.setUsername("test");
        user.setPassword("testPassw0rd!");
        user.setId(0L);
        return user;
    }
}

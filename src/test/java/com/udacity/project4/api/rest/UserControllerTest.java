package com.udacity.project4.api.rest;

import com.udacity.project4.api.model.CreateUserRequest;
import com.udacity.project4.domain.model.User;
import com.udacity.project4.domain.repository.CartRepository;
import com.udacity.project4.domain.repository.UserRepository;
import com.udacity.project4.infrastructure.exception.handler.ResourceNotFoundException;
import com.udacity.project4.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Valid;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

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
    public void getUserByIdIsSuccessful() throws ResourceNotFoundException {
        User user = createUser();
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(0L);
        User userResponse = response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testuser", userResponse.getUsername());
    }

    @Test
    public void getUserByIdThrowsErrorWhenInvalidIdSupplied() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<User> response = userController.findById(0L);
        });
        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getUserByUsernameIsSuccessful() throws ResourceNotFoundException {
        User user = createUser();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findByUserName("testuser");
        User userResponse = response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testuser", userResponse.getUsername());
    }

    @Test
    public void getUserByUsernameThrowsErrorWhenInvalidUsernameSupplied() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ResponseEntity<User> response = userController.findByUserName("nonexistinguser");
        });
        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private User createUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testPassw0rd!");
        user.setId(0L);
        return user;
    }
}

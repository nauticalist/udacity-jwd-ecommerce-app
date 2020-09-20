package com.udacity.project4.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.udacity.project4.Project4Application;
import com.udacity.project4.api.model.CreateUserRequest;

import com.udacity.project4.domain.repository.CartRepository;
import com.udacity.project4.domain.repository.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.udacity.project4.utils.JsonUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Project4Application.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static ObjectMapper objectMapper;


    @BeforeAll
    static void initAll() {
        // Initialize Jackson mapper to convert response json to object
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    }


    @Test
    public void createUserIsSuccessful() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testPassw0rd!");
        userRequest.setConfirmPassword("testPassw0rd!");

        mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(userRequest.getUsername())))

        ;
    }

    @Test
    public void createUserFailsWithDifferentPassword() throws Exception {

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testPassw0rd!");
        userRequest.setConfirmPassword("testPassw0rd!2");

        mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0].code", is(1030)))

        ;
    }

    @Test
    public void findByUserNameFailsWhenUserIsNotLoggedIn() throws Exception {

        mockMvc.perform(get("/api/user/" + "testUserX").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


}
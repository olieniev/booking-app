package com.olieniev.bookingapp.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olieniev.bookingapp.dto.user.CreateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.dto.user.UserLoginRequestDto;
import com.olieniev.bookingapp.dto.user.UserLoginResponseDto;
import com.olieniev.bookingapp.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
    }

    @Test
    @DisplayName("""
        Register user is successful
            """)
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void register_validUserData_success() throws Exception {
        CreateUserRequestDto requestDto = new CreateUserRequestDto(
                "register@email.com",
                "register",
                "me",
                "12345678"
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                post("/auth/register")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class
        );
        UserDto expected = new UserDto(
                null,
                "register@email.com",
                "register",
                "me",
                Role.ROLE_USER
        );
        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("""
        Login user with valid creds is successful
            """)
    @Sql(scripts = "classpath:database/users/insert-three-users.sql")
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void login_validUserData_success() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto(
                "user@email.com",
                "12345678"
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                post("/auth/login")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        UserLoginResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserLoginResponseDto.class
        );
        assertNotNull(actual);
    }
}

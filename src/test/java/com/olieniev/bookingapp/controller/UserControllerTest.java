package com.olieniev.bookingapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olieniev.bookingapp.dto.user.UpdateRoleRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleResponseDto;
import com.olieniev.bookingapp.dto.user.UpdateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
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
    @WithUserDetails("user20@email.com")
    @DisplayName("""
        Upgrade user role to manager is successful
            """)
    @Sql(scripts = "classpath:database/users/insert-three-users.sql")
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUserRole_validUserId_success() throws Exception {
        UpdateRoleRequestDto requestDto = new UpdateRoleRequestDto(Role.ROLE_MANAGER);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                put("/users/{id}/role", 20L)
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        UpdateRoleResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UpdateRoleResponseDto.class
        );
        UpdateRoleResponseDto expected = new UpdateRoleResponseDto(
                20L, "user20@email.com", Role.ROLE_MANAGER
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("user30@email.com")
    @DisplayName("""
        Upgrade user role to manager for different user is not successful
            """)
    @Sql(scripts = "classpath:database/users/insert-three-users.sql")
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUserRole_differentUserId_notSuccess() throws Exception {
        UpdateRoleRequestDto requestDto = new UpdateRoleRequestDto(Role.ROLE_MANAGER);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                put("/users/{id}/role", 40L)
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("user20@email.com")
    @DisplayName("""
        Get user info is successful
            """)
    @Sql(scripts = "classpath:database/users/insert-three-users.sql")
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserInfo_success() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class
        );
        UserDto expected = new UserDto(
                20L,
                "user20@email.com",
                "usertwenty",
                "usertwenty",
                Role.ROLE_MANAGER);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("user20@email.com")
    @DisplayName("""
        Upgrade user info is successful
            """)
    @Sql(scripts = "classpath:database/users/insert-three-users.sql")
    @Sql(scripts = "classpath:database/users/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUserInfo_validUserId_success() throws Exception {
        String expectedEmail = "updated@email.com";
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto(expectedEmail, null, null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                patch("/users/me")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class
        );
        assertNotNull(actual);
        assertEquals(expectedEmail, actual.email());
    }
}

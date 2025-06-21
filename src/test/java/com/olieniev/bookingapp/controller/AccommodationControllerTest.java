package com.olieniev.bookingapp.controller;

import static com.olieniev.bookingapp.util.AccommodationUtil.createAccommodationDto;
import static com.olieniev.bookingapp.util.AccommodationUtil.createAccommodationRequestDto;
import static com.olieniev.bookingapp.util.AccommodationUtil.getAlternativeDto;
import static com.olieniev.bookingapp.util.AccommodationUtil.getListOfDto;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.olieniev.bookingapp.dto.accommodation.UpdateAccommodationRequestDto;
import com.olieniev.bookingapp.service.notification.NotificationService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccommodationControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private NotificationService notificationService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithUserDetails("admin@email.com")
    @DisplayName("""
        Create a new accommodation with valid request dto is successful
            """)
    @Sql(scripts = {
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createAccommodation_validRequestDto_success() throws Exception {
        CreateAccommodationRequestDto requestDto = createAccommodationRequestDto();
        AccommodationDto expected = createAccommodationDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                post("/accommodations")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isCreated())
                .andReturn();
        AccommodationDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationDto.class
        );
        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithUserDetails("user@email.com")
    @DisplayName("""
        Create a new accommodation with valid request dto and user role is unsuccessful
            """)
    void createAccommodation_userRole_notSuccess() throws Exception {
        CreateAccommodationRequestDto requestDto = createAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                post("/accommodations")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isForbidden());

    }

    @Test
    @WithUserDetails("admin@email.com")
    @DisplayName("""
        Update an accommodation with valid update dto and id in path is successful
            """)
    @Sql(scripts = {
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql"
    })
    @Sql(scripts = {
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAccommodation_validUpdateRequestDto_success() throws Exception {
        UpdateAccommodationRequestDto updateDto = new UpdateAccommodationRequestDto(
                null, null, null, null, BigDecimal.valueOf(1000L), null);
        String jsonRequest = objectMapper.writeValueAsString(updateDto);
        MvcResult result = mockMvc.perform(
                patch("/accommodations/{id}", 10L)
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        AccommodationDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationDto.class
        );
        assertNotNull(actual);
        assertEquals(BigDecimal.valueOf(1000L), actual.dailyRate());
    }

    @Test
    @DisplayName("""
        Get existing accommodation by id is successful
            """)
    @Sql(scripts = {
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-three-accommodations.sql",
        "classpath:database/accommodations/insert-amenities-for-three-accommodations.sql"
    })
    @Sql(scripts = {
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAccommodation_validId_success() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/accommodations/{id}", 22L)
            )
                .andExpect(status().isOk())
                .andReturn();
        AccommodationDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationDto.class
        );
        AccommodationDto expected = getAlternativeDto();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
        Get all accommodations is successful
            """)
    @Sql(scripts = {
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-three-accommodations.sql",
        "classpath:database/accommodations/insert-amenities-for-three-accommodations.sql"
    })
    @Sql(scripts = {
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllAccommodations_success() throws Exception {
        List<AccommodationDto> expected = getListOfDto();
        MvcResult result = mockMvc.perform(
                get("/accommodations")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode contentNode = root.get("content");
        List<AccommodationDto> actual = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<AccommodationDto>>() {}
        );
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
    }

    @WithUserDetails("user20@email.com")
    @Test
    @DisplayName("""
        Deleting an existing accommodation is successful
            """)
    @Sql(scripts = {
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-three-accommodations.sql",
        "classpath:database/accommodations/insert-amenities-for-three-accommodations.sql"
    })
    @Sql(scripts = {
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteAccommodation_ExistsInDb_successful() throws Exception {
        mockMvc.perform(
                delete("/accommodations/{id}", 21L)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isNoContent());
    }
}

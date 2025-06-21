package com.olieniev.bookingapp.controller;

import static com.olieniev.bookingapp.util.BookingUtil.createBookingDto;
import static com.olieniev.bookingapp.util.BookingUtil.createBookingRequestDto;
import static com.olieniev.bookingapp.util.BookingUtil.getBookingsById;
import static com.olieniev.bookingapp.util.BookingUtil.getDetailedDto;
import static com.olieniev.bookingapp.util.BookingUtil.getUpdatedBooking;
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
import com.olieniev.bookingapp.dto.booking.BookingDto;
import com.olieniev.bookingapp.dto.booking.CreateBookingRequestDto;
import com.olieniev.bookingapp.dto.booking.DetailedBookingDto;
import com.olieniev.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.service.notification.NotificationService;
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
public class BookingControllerTest {
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
    @WithUserDetails("user30@email.com")
    @DisplayName("""
        Create a new booking with valid request dto is successful
            """)
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql"})
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createAccommodation_validRequestDto_success() throws Exception {
        CreateBookingRequestDto requestDto = createBookingRequestDto();
        BookingDto expected = createBookingDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                post("/bookings")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        BookingDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookingDto.class
        );
        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithUserDetails("admin@email.com")
    @DisplayName("""
        Get booking of user by admin is successful
            """)
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookings_ofUserByAdmin_success() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/bookings?user_id={id}&status={status}", 40L, "CONFIRMED")
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode contentNode = root.get("content");
        List<BookingDto> actual = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<BookingDto>>() {}
        );
        List<BookingDto> expected = getBookingsById();
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("user40@email.com")
    @DisplayName("""
        Get booking of user by user is successful
            """)
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookings_ofUser_success() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/bookings/my")
            )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode contentNode = root.get("content");
        List<BookingDto> actual = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<BookingDto>>() {}
        );
        List<BookingDto> expected = getBookingsById();
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("user20@email.com")
    @DisplayName("""
        Get booking of user by id is successful
            """)
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooking_byId_success() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/bookings/{id}", 13L)
            )
                .andExpect(status().isOk())
                .andReturn();
        DetailedBookingDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), DetailedBookingDto.class
        );
        DetailedBookingDto expected = getDetailedDto();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("user20@email.com")
    @DisplayName("""
        Update booking is successful
            """)
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBooking_byOwner_success() throws Exception {
        UpdateBookingRequestDto requestDto = new UpdateBookingRequestDto(Booking.Status.CANCELED);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                patch("/bookings/{id}", 11L)
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        BookingDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookingDto.class
        );
        BookingDto expected = getUpdatedBooking();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("admin@email.com")
    @DisplayName("""
        Delete booking by admin is successful
            """)
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBooking_ExistsInDb_successful() throws Exception {
        mockMvc.perform(
                delete("/bookings/{id}", 12L)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isNoContent());
    }
}

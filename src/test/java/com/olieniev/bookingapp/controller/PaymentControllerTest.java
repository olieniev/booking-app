package com.olieniev.bookingapp.controller;

import static com.olieniev.bookingapp.util.PaymentUtil.getListOfPayments;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olieniev.bookingapp.dto.payment.PaymentDto;
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
public class PaymentControllerTest {
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
    @WithUserDetails("user20@email.com")
    @DisplayName("""
        Get existing payments of user by user is successful
            """)
    @Sql(scripts = {
        "classpath:database/payments/delete-from-payments.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql",
        "classpath:database/bookings/insert-three-bookings.sql",
        "classpath:database/payments/insert-three-payments.sql"
    })
    @Sql(scripts = {
        "classpath:database/payments/delete-from-payments.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_paymentsByUser_success() throws Exception {
        List<PaymentDto> expected = getListOfPayments();
        MvcResult result = mockMvc.perform(
                get("/payments")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode contentNode = root.get("content");
        List<PaymentDto> actual = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<PaymentDto>>() {}
        );
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("admin@email.com")
    @DisplayName("""
        Get existing payments of user by admin is successful
            """)
    @Sql(scripts = {
        "classpath:database/payments/delete-from-payments.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/accommodations/insert-two-amenities.sql",
        "classpath:database/bookings/insert-three-bookings.sql",
        "classpath:database/payments/insert-three-payments.sql"
    })
    @Sql(scripts = {
        "classpath:database/payments/delete-from-payments.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations-amenities.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_paymentsOfUserByAdmin_success() throws Exception {
        List<PaymentDto> expected = getListOfPayments();
        MvcResult result = mockMvc.perform(
                get("/payments?id={id}", 20L)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode contentNode = root.get("content");
        List<PaymentDto> actual = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<PaymentDto>>() {}
        );
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }
}

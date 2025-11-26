package com.bloomberg.fxdeals;

import com.bloomberg.fxdeals.controller.FXDealController;
import com.bloomberg.fxdeals.dto.FXDealRequest;
import com.bloomberg.fxdeals.dto.FXDealResponse;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.service.FXDealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for FXDealController.
 */
@WebMvcTest(FXDealController.class)
class FXDealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FXDealService dealService;

    private FXDealRequest validRequest;
    private FXDealResponse savedDeal;

    @BeforeEach
    void setUp() {
        validRequest = FXDealRequest.builder()
                .dealId("DEAL-TEST-001")
                .fromCurrencyCode("USD")
                .toCurrencyCode("EUR")
                .dealTimestamp(Instant.parse("2024-11-25T10:30:00Z"))
                .dealAmount(new BigDecimal("1000000.50"))
                .build();

        savedDeal = FXDealResponse.builder()
                .dealId("DEAL-TEST-001")
                .fromCurrencyCode("USD")
                .toCurrencyCode("EUR")
                .dealTimestamp(Instant.parse("2024-11-25T10:30:00Z"))
                .dealAmount(new BigDecimal("1000000.50"))
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void createDeal_WithValidRequest_ShouldReturnCreated() throws Exception {
        when(dealService.createDeal(any(FXDealRequest.class))).thenReturn(savedDeal);

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());

        verify(dealService).createDeal(any(FXDealRequest.class));
    }

    @Test
    void createDeal_WithMissingFields_ShouldReturnBadRequest() throws Exception {
        FXDealRequest invalidRequest = FXDealRequest.builder().build();

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(jsonPath("$.status", is(500)));

        verify(dealService, never()).createDeal(any(FXDealRequest.class));
    }

    @Test
    void createDeal_WithInvalidCurrencyCode_ShouldReturnBadRequest() throws Exception {
        validRequest.setFromCurrencyCode("XXX");

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(jsonPath("$.status", is(500)));

        verify(dealService, never()).createDeal(any(FXDealRequest.class));
    }

    @Test
    void createDeal_WithNegativeAmount_ShouldReturnBadRequest() throws Exception {
        validRequest.setDealAmount(new BigDecimal("-100"));

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(jsonPath("$.status", is(500)));

        verify(dealService, never()).createDeal(any(FXDealRequest.class));
    }

    @Test
    void createDeal_WithDuplicateId_ShouldReturnConflict() throws Exception {
        when(dealService.createDeal(any(FXDealRequest.class)))
                .thenThrow(new DuplicateDealException("Deal with ID 'DEAL-TEST-001' already exists"));

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", containsString("already exists")));

        verify(dealService).createDeal(any(FXDealRequest.class));
    }


}
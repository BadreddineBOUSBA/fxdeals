package com.bloomberg.fxdeals;

import com.bloomberg.fxdeals.dto.FXDealRequest;
import com.bloomberg.fxdeals.dto.FXDealResponse;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.mapper.FXDealMapper;
import com.bloomberg.fxdeals.model.FXDeal;
import com.bloomberg.fxdeals.repository.FXDealRepository;
import com.bloomberg.fxdeals.service.FXDealService;
import com.bloomberg.fxdeals.service.impl.FXDealServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FXDealService.
 */
@ExtendWith(MockitoExtension.class)
class FXDealServiceTest {

    @Mock
    private FXDealRepository dealRepository;

    @Mock
    private FXDealMapper fxDealMapper;

    @InjectMocks
    private FXDealServiceImpl dealService;

    private FXDealRequest validRequest;
    private FXDeal savedDeal;
    private FXDealResponse expectedResponse;



    @BeforeEach
    void setUp() {

        // Setup test data
        validRequest = FXDealRequest.builder()
                .dealId("DEAL-TEST-001")
                .fromCurrencyCode("USD")
                .toCurrencyCode("EUR")
                .dealTimestamp(Instant.parse("2024-11-25T10:30:00Z"))
                .dealAmount(new BigDecimal("1000000.50"))
                .build();

        savedDeal = FXDeal.builder()
                .id(1L)
                .dealId("DEAL-TEST-001")
                .fromCurrencyCode("USD")
                .toCurrencyCode("EUR")
                .dealTimestamp(Instant.parse("2024-11-25T10:30:00Z"))
                .dealAmount(new BigDecimal("1000000.50"))
                .createdAt(Instant.now())
                .build();

        expectedResponse = FXDealResponse.builder()
                .dealId("DEAL-TEST-001")
                .fromCurrencyCode("USD")
                .toCurrencyCode("EUR")
                .dealTimestamp(Instant.parse("2024-11-25T10:30:00Z"))
                .dealAmount(new BigDecimal("1000000.50"))
                .createdAt(savedDeal.getCreatedAt())
                .build();
    }


    @Test
    void createDeal_WithValidRequest_ShouldReturnDealResponse() throws Exception {
        when(dealRepository.existsByDealId(anyString())).thenReturn(false);
        when(fxDealMapper.toEntity(any(FXDealRequest.class))).thenReturn(savedDeal);
        when(dealRepository.save(any(FXDeal.class))).thenReturn(savedDeal);
        when(fxDealMapper.toResponse(any(FXDeal.class))).thenReturn(expectedResponse);


        FXDealResponse result = dealService.createDeal(validRequest);

        assertThat(result).isNotNull();
        assertThat(result.getDealId()).isEqualTo("DEAL-TEST-001");
        assertThat(result.getFromCurrencyCode()).isEqualTo("USD");
        assertThat(result.getToCurrencyCode()).isEqualTo("EUR");
        assertThat(result.getDealAmount()).isEqualByComparingTo(new BigDecimal("1000000.50"));
        assertThat(result.getDealTimestamp()).isEqualTo(Instant.parse("2024-11-25T10:30:00Z"));
        assertThat(result.getCreatedAt()).isNotNull();

        verify(dealRepository).existsByDealId("DEAL-TEST-001");
        verify(fxDealMapper).toEntity(validRequest);
        verify(dealRepository).save(any(FXDeal.class));
        verify(fxDealMapper).toResponse(savedDeal);
    }

    @Test
    void createDeal_WithDuplicateId_ShouldThrowDuplicateDealException() {
        FXDeal mappedDeal = FXDeal.builder()
                .dealId("DEAL-TEST-001")
                .fromCurrencyCode("USD")
                .toCurrencyCode("EUR")
                .dealTimestamp(validRequest.getDealTimestamp())
                .dealAmount(validRequest.getDealAmount())
                .build();

        when(fxDealMapper.toEntity(validRequest)).thenReturn(mappedDeal);
        when(dealRepository.existsByDealId("DEAL-TEST-001")).thenReturn(true);

        assertThatThrownBy(() -> dealService.createDeal(validRequest))
                .isInstanceOf(DuplicateDealException.class)
                .hasMessageContaining("DEAL-TEST-001")
                .hasMessageContaining("already exists");

        verify(dealRepository).existsByDealId("DEAL-TEST-001");
        verify(dealRepository, never()).save(any(FXDeal.class));
    }

    @Test
    void createDeal_ShouldMapAllFieldsCorrectly() throws Exception {
        when(dealRepository.existsByDealId(anyString())).thenReturn(false);
        when(fxDealMapper.toEntity(any(FXDealRequest.class))).thenReturn(savedDeal);
        when(dealRepository.save(any(FXDeal.class))).thenAnswer(invocation -> {
            FXDeal deal = invocation.getArgument(0);
            deal.setId(1L);
            deal.setCreatedAt(Instant.now());
            return deal;
        });
        when(fxDealMapper.toResponse(any(FXDeal.class))).thenReturn(expectedResponse);

        FXDealResponse result = dealService.createDeal(validRequest);

        assertThat(result.getDealId()).isEqualTo(validRequest.getDealId());
        assertThat(result.getFromCurrencyCode()).isEqualTo(validRequest.getFromCurrencyCode());
        assertThat(result.getToCurrencyCode()).isEqualTo(validRequest.getToCurrencyCode());
        assertThat(result.getDealTimestamp()).isEqualTo(validRequest.getDealTimestamp());
        assertThat(result.getDealAmount()).isEqualByComparingTo(validRequest.getDealAmount());
    }

    @Test
    void createDeal_WithMinimumAmount_ShouldSucceed() throws Exception {
        validRequest.setDealAmount(new BigDecimal("0.01"));
        when(dealRepository.existsByDealId(anyString())).thenReturn(false);
        when(fxDealMapper.toEntity(any(FXDealRequest.class))).thenReturn(savedDeal);
        when(dealRepository.save(any(FXDeal.class))).thenReturn(savedDeal);
        when(fxDealMapper.toResponse(any(FXDeal.class))).thenReturn(expectedResponse);

        FXDealResponse result = dealService.createDeal(validRequest);

        assertThat(result).isNotNull();
        verify(dealRepository).save(any(FXDeal.class));
    }

    @Test
    void createDeal_WithLargeAmount_ShouldSucceed() throws Exception {
        validRequest.setDealAmount(new BigDecimal("999999999999999.99"));
        when(dealRepository.existsByDealId(anyString())).thenReturn(false);
        when(fxDealMapper.toEntity(any(FXDealRequest.class))).thenReturn(savedDeal);
        when(dealRepository.save(any(FXDeal.class))).thenReturn(savedDeal);
        when(fxDealMapper.toResponse(any(FXDeal.class))).thenReturn(expectedResponse);

        FXDealResponse result = dealService.createDeal(validRequest);

        assertThat(result).isNotNull();
        verify(dealRepository).save(any(FXDeal.class));
    }

    @Test
    void createDeal_WithDifferentCurrencies_ShouldSucceed() throws Exception {
        validRequest.setFromCurrencyCode("EUR");
        validRequest.setToCurrencyCode("USD");
        when(dealRepository.existsByDealId(anyString())).thenReturn(false);
        when(fxDealMapper.toEntity(any(FXDealRequest.class))).thenReturn(savedDeal);
        when(dealRepository.save(any(FXDeal.class))).thenReturn(savedDeal);
        when(fxDealMapper.toResponse(any(FXDeal.class))).thenReturn(expectedResponse);

        FXDealResponse result = dealService.createDeal(validRequest);

        assertThat(result).isNotNull();
        verify(dealRepository).save(any(FXDeal.class));
    }

}
package com.bloomberg.fxdeals.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class FXDealRequest {

    @NotBlank
    private String dealId;

    @NotBlank
    private String fromCurrencyCode;

    @NotBlank
    private String toCurrencyCode;

    @NotNull
    private Instant dealTimestamp;

    @NotNull
    private BigDecimal dealAmount;
}

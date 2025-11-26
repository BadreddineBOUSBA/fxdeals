package com.bloomberg.fxdeals.dto;

import com.bloomberg.fxdeals.validator.ValidCurrencyCode;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class FXDealRequest {

    @NotBlank
    @Size(max = 100)
    private String dealId;

    @NotBlank
    @ValidCurrencyCode // to use the annotation with our specific rules
    private String fromCurrencyCode;

    @NotBlank
    @ValidCurrencyCode
    private String toCurrencyCode;

    @NotNull
    private Instant dealTimestamp;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal dealAmount;
}

package com.bloomberg.fxdeals.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class FXDealResponse {

    String dealId;
    String fromCurrencyCode;
    String toCurrencyCode;
    Instant dealTimestamp;
    BigDecimal dealAmount;
    Instant createdAt;
}

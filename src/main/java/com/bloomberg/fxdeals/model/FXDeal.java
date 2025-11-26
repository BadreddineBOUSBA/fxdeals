package com.bloomberg.fxdeals.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entity representing an FX (Foreign Exchange) deal in the data warehouse.
 *
 * This entity stores all details about currency exchange deals including
 * unique identifiers, currency codes, timestamps, and amounts.
 */
@Entity
@Table(name = "fx_deals", indexes = {
        @Index(name = "idx_deal_unique_id", columnList = "deal_unique_id", unique = true),
        @Index(name = "idx_deal_timestamp", columnList = "deal_timestamp"),
        @Index(name = "idx_currency_codes", columnList = "from_currency_code, to_currency_code")
})
@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FXDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "deal_id", nullable = false, unique = true, length = 100)
    private String dealId;


    @Column(name = "from_currency_code", nullable = false, length = 3)
    private String fromCurrencyCode;


    @Column(name = "to_currency_code", nullable = false, length = 3)
    private String toCurrencyCode;


    @Column(name = "deal_timestamp", nullable = false)
    private Instant dealTimestamp;


    @Column(name = "deal_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal dealAmount;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

}
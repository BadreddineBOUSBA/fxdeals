package com.bloomberg.fxdeals.mapper;


import com.bloomberg.fxdeals.dto.FXDealRequest;
import com.bloomberg.fxdeals.dto.FXDealResponse;
import com.bloomberg.fxdeals.model.FXDeal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FXDealMapper {

    public FXDeal toEntity(FXDealRequest request) {
        if (request == null) {
            return null;
        }
        return FXDeal.builder()
                .dealId(request.getDealId())
                .fromCurrencyCode(request.getFromCurrencyCode())
                .toCurrencyCode(request.getToCurrencyCode())
                .dealTimestamp(request.getDealTimestamp())
                .dealAmount(request.getDealAmount())
                .build();
    }

    public FXDealResponse toResponse(FXDeal deal) {
        if (deal == null) {
            return null;
        }
        return FXDealResponse.builder()
                .dealId(deal.getDealId())
                .fromCurrencyCode(deal.getFromCurrencyCode())
                .toCurrencyCode(deal.getToCurrencyCode())
                .dealTimestamp(deal.getDealTimestamp())
                .dealAmount(deal.getDealAmount())
                .createdAt(deal.getCreatedAt())
                .build();
    }

    public List<FXDeal> toEntityList(List<FXDealRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<FXDealResponse> toResponseList(List<FXDeal> deals) {
        if (deals == null) {
            return List.of();
        }
        return deals.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

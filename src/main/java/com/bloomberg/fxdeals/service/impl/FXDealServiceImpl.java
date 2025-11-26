package com.bloomberg.fxdeals.service.impl;

import com.bloomberg.fxdeals.dto.FXDealRequest;
import com.bloomberg.fxdeals.dto.FXDealResponse;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.mapper.FXDealMapper;
import com.bloomberg.fxdeals.model.FXDeal;
import com.bloomberg.fxdeals.repository.FXDealRepository;
import com.bloomberg.fxdeals.service.FXDealService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public abstract class FXDealServiceImpl implements FXDealService {

    private final FXDealRepository fxDealRepository;
    private final FXDealMapper fxDealMapper;

    @Override
    public FXDealResponse createDeal(FXDealRequest dealRequest) throws Exception {
        FXDeal deal = fxDealMapper.toEntity(dealRequest);

        validateDealAmount(deal);


        if (fxDealRepository.existsByDealId(deal.getDealId())) {
            throw new DuplicateDealException("Deal with id " + deal.getDealId() + " already exists");
        }

        try {
            FXDeal saved = fxDealRepository.save(deal);
            return fxDealMapper.toResponse(saved);
        } catch (Exception ex) {
            throw new Exception("An error occured while saving the deal : " + ex.getMessage());
        }
    }


    private void validateDealAmount(FXDeal deal) {

        //we can here add more validation logic

        BigDecimal amount = deal.getDealAmount();
        if (amount.signum() <= 0) {
            throw new RuntimeException("Deal amount must be positive");
        }
    }

}

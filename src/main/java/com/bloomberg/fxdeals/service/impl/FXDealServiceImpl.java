package com.bloomberg.fxdeals.service.impl;

import com.bloomberg.fxdeals.dto.FXDealRequest;
import com.bloomberg.fxdeals.dto.FXDealResponse;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.exception.InvalidRequestException;
import com.bloomberg.fxdeals.mapper.FXDealMapper;
import com.bloomberg.fxdeals.model.FXDeal;
import com.bloomberg.fxdeals.repository.FXDealRepository;
import com.bloomberg.fxdeals.service.FXDealService;
import com.bloomberg.fxdeals.validator.CurrencyCodeValidatorUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@AllArgsConstructor
public class FXDealServiceImpl implements FXDealService {

    private final FXDealRepository fxDealRepository;
    private final FXDealMapper fxDealMapper;

    private static final Logger log = LoggerFactory.getLogger(FXDealServiceImpl.class);

    @Override
    public FXDealResponse createDeal(FXDealRequest dealRequest) throws Exception {
        FXDeal deal = fxDealMapper.toEntity(dealRequest);

        validateDealAmount(deal);
        CurrencyCodeValidatorUtil.validate(dealRequest.getToCurrencyCode());
        CurrencyCodeValidatorUtil.validate(dealRequest.getFromCurrencyCode());

        if (fxDealRepository.existsByDealId(deal.getDealId())) {
            log.warn("Duplicate deal detected: {}", deal.getDealId());
            throw new DuplicateDealException("Deal with id " + deal.getDealId() + " already exists");
        }

        try {
            FXDeal saved = fxDealRepository.save(deal);
            log.info("Deal created successfully : {}", deal.getDealId());
            return fxDealMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("An error occured while saving the deal : {}", ex.getMessage());
            throw new Exception("An error occured while saving the deal : " + ex.getMessage());
        }
    }


    private void validateDealAmount(FXDeal deal) {

        //we can here add more validation logic

        BigDecimal amount = deal.getDealAmount();
        if (amount.signum() <= 0) {
            throw new InvalidRequestException("Deal amount must be positive");
        }
    }

}

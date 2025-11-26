package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dto.FXDealRequest;
import com.bloomberg.fxdeals.dto.FXDealResponse;



public interface FXDealService {
    FXDealResponse createDeal(FXDealRequest deal) throws Exception;

}

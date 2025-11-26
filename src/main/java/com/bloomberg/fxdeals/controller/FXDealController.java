package com.bloomberg.fxdeals.controller;


import com.bloomberg.fxdeals.dto.FXDealRequest;
import com.bloomberg.fxdeals.dto.FXDealResponse;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.exception.GlobalExceptionHandler;
import com.bloomberg.fxdeals.exception.InvalidRequestException;
import com.bloomberg.fxdeals.service.FXDealService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import org.springframework.http.HttpStatus;



@RestController
@RequestMapping("/api/v1/deals")
@AllArgsConstructor
public class FXDealController {


    private final FXDealService dealService;

    private final GlobalExceptionHandler globalExceptionHandler;


    @PostMapping
    public ResponseEntity<?> createDeal(@Valid @RequestBody FXDealRequest request) throws Exception {
        try{
            FXDealResponse createdDeal = dealService.createDeal(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdDeal);
        }catch(DuplicateDealException exception){
             return   globalExceptionHandler.handleDuplicateDealException(exception);
        }catch(InvalidRequestException exception){
             return   globalExceptionHandler.invalidRequestException(exception);
        }catch(Exception exception){
            // to handle any other technical exception thrown
            return   globalExceptionHandler.handleGenericException(exception);
        }
    }



}


package com.bloomberg.fxdeals.controller;


import com.bloomberg.fxdeals.dto.FXDealRequest;
import com.bloomberg.fxdeals.dto.FXDealResponse;
import com.bloomberg.fxdeals.service.FXDealService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/v1/deals")
@Slf4j
@AllArgsConstructor
public class FXDealController {


    private final FXDealService dealService;



    @PostMapping
    public ResponseEntity<FXDealResponse> createDeal(@Valid @RequestBody FXDealRequest request) throws Exception {

        FXDealResponse createdDeal = dealService.createDeal(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdDeal);
    }



}


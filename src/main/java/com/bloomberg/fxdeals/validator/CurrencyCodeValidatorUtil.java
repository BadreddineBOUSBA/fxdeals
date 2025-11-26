package com.bloomberg.fxdeals.validator;

import com.bloomberg.fxdeals.exception.InvalidRequestException;

import java.util.Set;

public final class CurrencyCodeValidatorUtil {

    private static final Set<String> VALID_CURRENCY_CODES = Set.of(
            "USD", "EUR", "GBP"
            // just major currencies for test
    );

    private CurrencyCodeValidatorUtil() {
    }

    public static void validate(String value) {


        if (!value.equals(value.toUpperCase())) {
            throw new InvalidRequestException("Currency code must be uppercase (e.g. 'USD')");
        }

        if (!VALID_CURRENCY_CODES.contains(value)) {
            throw new InvalidRequestException("Unsupported currency code: " + value);
        }
    }
}
package com.bloomberg.fxdeals.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Set;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCurrencyCode.CurrencyCodeValidator.class)
@Documented
public @interface ValidCurrencyCode {

    String message() default "Invalid  currency code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {

        private static final Set<String> VALID_CURRENCY_CODES = Set.of(

                "USD", "EUR", "GBP"
                // just major currencies for test
        );

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isBlank()) {
                return false;
            }

            if (value.length() != 3) {
                return false;
            }

            if (!value.equals(value.toUpperCase())) {
                return false;
            }

            return VALID_CURRENCY_CODES.contains(value);
        }
    }
}

package com.bsoetaert.net_worth_calculator;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTests {

    @Test
    public void validateUserId() {
        DataStore.reset();

        assertThrows(ResponseStatusException.class, () -> Validator.validateUserId(1));
        assertThrows(ResponseStatusException.class, () -> Validator.validateUserId(null));

        Integer userId = DataStore.addUser();
        assertDoesNotThrow(() -> Validator.validateUserId(userId));
    }

    @Test
    public void validateCurrency() {
        DataStore.reset();

        assertThrows(ResponseStatusException.class, () -> Validator.validateCurrency("   "));
        assertThrows(ResponseStatusException.class, () -> Validator.validateCurrency(null));
        assertThrows(ResponseStatusException.class, () -> Validator.validateCurrency("TOOLONG"));

        assertDoesNotThrow(() -> Validator.validateCurrency("CAD"));
        assertDoesNotThrow(() -> Validator.validateCurrency("AAA"));
    }
}

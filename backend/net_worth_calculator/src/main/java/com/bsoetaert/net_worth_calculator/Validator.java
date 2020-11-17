package com.bsoetaert.net_worth_calculator;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Validator {

    public static void validateUserId(Integer userId) {
        if(userId == null || !DataStore.hasUserSession(userId))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User session does not exist. Call /user first.");
        }
    }

    public static void validateCurrency(String currency) {
        if(currency == null || currency.length() != 3)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency element must be a 3 character identifier.");
        }
    }
}

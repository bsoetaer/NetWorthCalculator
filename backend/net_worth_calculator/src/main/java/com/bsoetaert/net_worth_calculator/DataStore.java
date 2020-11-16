package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.Accounting;
import com.bsoetaert.net_worth_calculator.model.AccountingItem;
import com.bsoetaert.net_worth_calculator.model.AccountingSheet;
import com.bsoetaert.net_worth_calculator.model.AccountingValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class DataStore {
    private static Map<String, CurrencyRate> exchangeRates = new HashMap<>();

    private static Map<Integer, AccountingSheet> userSessions = new HashMap<>();

    private static Integer nextUserId = 1;

    public static Map<String, CurrencyRate> getExchangeRates() {
        return exchangeRates;
    }

    public static void setExchangeRates(Map<String, CurrencyRate> exchangeRates) {
        DataStore.exchangeRates = exchangeRates;
    }

    public static void reset() {
        exchangeRates = new HashMap<>();
        userSessions = new HashMap<>();
        nextUserId = 1;
    }

    public static boolean hasUserSession(Integer userId) {
        return userSessions.containsKey(userId);
    }

    public static Integer addUser() {
        Integer userId = nextUserId++;
        userSessions.put(userId, createBaseData());
        return userId;
    }

    public static AccountingSheet getSheet(Integer userId) {
        return userSessions.get(userId);
    }

    private static AccountingSheet createBaseData() {
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File(DataStore.class.getResource("/static/BaseData.json").getPath());

        AccountingSheet sheet = null;

        try {
            sheet = objectMapper.readValue(file, AccountingSheet.class);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read base data.", e);
        }

        return sheet;
    }
}

package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.AccountingSheet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.*;

public class DataStore {
    private static CurrencyExchange exchange = null;

    private static Map<Integer, AccountingSheet> userSessions = new HashMap<>();

    private static Integer nextUserId = 1;

    public static CurrencyExchange getExchange() {
        if(exchange == null) {
            Collection<IRateProvider> rateProviders = new ArrayList<IRateProvider>();
            rateProviders.add(new ExchangeRatesApiProvider());
            exchange = new CurrencyExchange(rateProviders);
        }
        return exchange;
    }

    public static void setExchange(CurrencyExchange exchange) {
        DataStore.exchange = exchange;
    }

    public static void reset() {
        exchange = null;
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

    public static void setSheet(Integer userId, AccountingSheet sheet) {
        userSessions.put(userId, sheet);
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

package com.bsoetaert.net_worth_calculator;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class CurrencyExchangeTests {
    @Test
    public void updateRatesFail() {
        CurrencyExchange exchange = new CurrencyExchange(new ArrayList<>());
        assertThrows(ResponseStatusException.class, () -> exchange.updateRates("CAD"));
    }

    @Test
    public void updateRatesFirstProviderSucceeds() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";
        BigDecimal startValue = new BigDecimal(String.valueOf(13.62));
        BigDecimal exchangeRate1 = new BigDecimal(String.valueOf(1.5));
        BigDecimal exchangeRate2 = new BigDecimal(String.valueOf(2.0));

        CurrencyRate rate1 = new CurrencyRate(targetCurrency);
        rate1.addStartCurrency(baseCurrency, exchangeRate1);
        IRateProvider provider1 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider1.getName()).thenReturn("Mock1");
        Mockito.when(provider1.getRates(anyString())).thenReturn(rate1);

        CurrencyRate rate2 = new CurrencyRate(targetCurrency);
        rate2.addStartCurrency(baseCurrency, exchangeRate2);
        IRateProvider provider2 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider2.getName()).thenReturn("Mock2");
        Mockito.when(provider2.getRates(anyString())).thenReturn(rate2);
        ArrayList<IRateProvider> providerList = new ArrayList<>();
        providerList.add(provider1);
        providerList.add(provider2);

        CurrencyExchange exchange = new CurrencyExchange(providerList);

        assertFalse(exchange.canConvert(baseCurrency, targetCurrency));
        assertThrows(NullPointerException.class, () -> exchange.convert(startValue, baseCurrency, targetCurrency));

        exchange.updateRates("CAD");
        assertTrue(exchange.canConvert(baseCurrency, targetCurrency));
        assertEquals(startValue.multiply(exchangeRate1), exchange.convert(startValue, baseCurrency, targetCurrency));
    }

    @Test
    public void updateRatesFirstProviderFails() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";
        BigDecimal startValue = new BigDecimal(String.valueOf(13.62));
        BigDecimal exchangeRate2 = new BigDecimal(String.valueOf(2.0));

        IRateProvider provider1 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider1.getName()).thenReturn("Mock1");
        Mockito.when(provider1.getRates(anyString())).thenReturn(null);

        CurrencyRate rate2 = new CurrencyRate(targetCurrency);
        rate2.addStartCurrency(baseCurrency, exchangeRate2);
        IRateProvider provider2 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider2.getName()).thenReturn("Mock2");
        Mockito.when(provider2.getRates(anyString())).thenReturn(rate2);
        ArrayList<IRateProvider> providerList = new ArrayList<>();
        providerList.add(provider1);
        providerList.add(provider2);

        CurrencyExchange exchange = new CurrencyExchange(providerList);

        assertFalse(exchange.canConvert(baseCurrency, targetCurrency));
        assertThrows(NullPointerException.class, () -> exchange.convert(startValue, baseCurrency, targetCurrency));

        exchange.updateRates("CAD");
        assertTrue(exchange.canConvert(baseCurrency, targetCurrency));
        assertEquals(startValue.multiply(exchangeRate2), exchange.convert(startValue, baseCurrency, targetCurrency));

    }

    @Test
    public void updateRatesFailsAndUsesOldValues() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";
        BigDecimal startValue = new BigDecimal(String.valueOf(13.62));
        BigDecimal exchangeRate1 = new BigDecimal(String.valueOf(1.5));

        CurrencyRate rate1 = new CurrencyRate(targetCurrency);
        rate1.addStartCurrency(baseCurrency, exchangeRate1);
        LocalDate today = LocalDate.now();
        LocalDate oneDayAgo = today.minusDays(1);
        rate1.setLastUpdated(oneDayAgo);
        HashMap<String, CurrencyRate> startingRates = new HashMap<>();
        startingRates.put(targetCurrency, rate1);

        CurrencyExchange exchange = new CurrencyExchange(new ArrayList<>(), startingRates);
        exchange.canConvert(baseCurrency, targetCurrency);

        assertEquals(startValue.multiply(exchangeRate1), exchange.convert(startValue, baseCurrency, targetCurrency));
    }

    @Test
    public void updateRatesDoesNotUpdateWhenAlreadyUpToDate() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";
        BigDecimal startValue = new BigDecimal(String.valueOf(13.62));
        BigDecimal exchangeRate1 = new BigDecimal(String.valueOf(1.5));

        CurrencyRate rate1 = new CurrencyRate(targetCurrency);
        rate1.addStartCurrency(baseCurrency, exchangeRate1);
        LocalDate today = LocalDate.now();
        rate1.setLastUpdated(today);
        HashMap<String, CurrencyRate> startingRates = new HashMap<>();
        startingRates.put(targetCurrency, rate1);

        BigDecimal exchangeRate2 = new BigDecimal(String.valueOf(2.0));

        CurrencyRate rate2 = new CurrencyRate(targetCurrency);
        rate2.addStartCurrency(baseCurrency, exchangeRate1);
        IRateProvider provider1 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider1.getName()).thenReturn("Mock1");
        Mockito.when(provider1.getRates(anyString())).thenReturn(rate2);
        Mockito.verify(provider1, times(0)).getRates(anyString());
        ArrayList<IRateProvider> providerList = new ArrayList<>();
        providerList.add(provider1);

        CurrencyExchange exchange = new CurrencyExchange(providerList, startingRates);
        exchange.canConvert(baseCurrency, targetCurrency);

        assertEquals(startValue.multiply(exchangeRate1), exchange.convert(startValue, baseCurrency, targetCurrency));
    }

    @Test
    public void canConvertFail() {
        CurrencyExchange exchange = new CurrencyExchange(new ArrayList<>());
        exchange.canConvert("CAD", "CAD");
    }

    @Test
    public void useStartingRates() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";
        BigDecimal startValue = new BigDecimal(String.valueOf(13.62));
        BigDecimal exchangeRate1 = new BigDecimal(String.valueOf(1.5));

        CurrencyRate rate1 = new CurrencyRate(targetCurrency);
        rate1.addStartCurrency(baseCurrency, exchangeRate1);
        HashMap<String, CurrencyRate> startingRates = new HashMap<>();
        startingRates.put(targetCurrency, rate1);

        CurrencyExchange exchange = new CurrencyExchange(new ArrayList<>(), startingRates);
        exchange.canConvert(baseCurrency, targetCurrency);

        assertEquals(startValue.multiply(exchangeRate1), exchange.convert(startValue, baseCurrency, targetCurrency));
    }
}

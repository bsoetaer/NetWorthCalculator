package com.bsoetaert.net_worth_calculator;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

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

        IRateProvider provider = Mockito.mock(IRateProvider.class);
        Mockito.when(provider.getName()).thenReturn("Mock");
        Mockito.when(provider.getRates(anyString())).thenReturn(rate1);
        ArrayList<IRateProvider> providerList = new ArrayList<>();
        providerList.add(provider);

        CurrencyExchange exchange = new CurrencyExchange(providerList);
        exchange.updateRates(targetCurrency);
        exchange.setRateProviders(new ArrayList<>());
        exchange.updateRates(targetCurrency);

        assertTrue(exchange.canConvert(baseCurrency, targetCurrency));
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

        BigDecimal exchangeRate2 = new BigDecimal(String.valueOf(2.0));
        CurrencyRate rate2 = new CurrencyRate(targetCurrency);
        rate2.addStartCurrency(baseCurrency, exchangeRate2);

        IRateProvider provider1 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider1.getName()).thenReturn("Mock1");
        Mockito.when(provider1.getRates(anyString())).thenReturn(rate1);
        ArrayList<IRateProvider> providerList1 = new ArrayList<>();
        providerList1.add(provider1);

        IRateProvider provider2 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider2.getName()).thenReturn("Mock1");
        Mockito.when(provider2.getRates(anyString())).thenReturn(rate2);
        Mockito.verify(provider2, times(0)).getRates(anyString());
        ArrayList<IRateProvider> providerList2 = new ArrayList<>();
        providerList2.add(provider2);

        CurrencyExchange exchange = new CurrencyExchange(providerList1);

        exchange.updateRates(targetCurrency);
        exchange.setRateProviders(providerList2);
        exchange.updateRates(targetCurrency);

        assertEquals(startValue.multiply(exchangeRate1), exchange.convert(startValue, baseCurrency, targetCurrency));
    }

    @Test
    public void canConvertFail() {
        CurrencyExchange exchange = new CurrencyExchange(new ArrayList<>());
        assertTrue(exchange.canConvert("CAD", "CAD"));
        assertFalse(exchange.canConvert("CAD", "USD"));
    }
}

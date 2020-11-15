package com.bsoetaert.net_worth_calculator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyRateTests {
    @Test
    public void canConvert() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";
        CurrencyRate rate = new CurrencyRate(targetCurrency);

        assertTrue(rate.canConvert(targetCurrency));
        assertFalse(rate.canConvert(baseCurrency));

        rate.addStartCurrency(baseCurrency, new BigDecimal(String.valueOf(1.3)));
        assertTrue(rate.canConvert(baseCurrency));
    }

    @Test
    public void convertToItself() {
        String targetCurrency = "CAD";
        BigDecimal startValue = new BigDecimal(String.valueOf(5.63));
        CurrencyRate rate = new CurrencyRate(targetCurrency);

        assertEquals(rate.convertFrom(startValue, targetCurrency), startValue);
    }

    @Test
    public void convertToAnother() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";
        BigDecimal startValue = new BigDecimal(String.valueOf(5.63));
        BigDecimal conversionRate = new BigDecimal(String.valueOf(1.34));
        CurrencyRate rate = new CurrencyRate(targetCurrency);
        rate.addStartCurrency(baseCurrency, conversionRate);

        assertEquals(startValue.multiply(conversionRate), rate.convertFrom(startValue, baseCurrency));
    }

    @Test
    public void convertMissingCurrency() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";
        BigDecimal startValue = new BigDecimal(String.valueOf(5.63));
        CurrencyRate rate = new CurrencyRate(targetCurrency);

        assertThrows(NullPointerException.class, () -> rate.convertFrom(startValue, baseCurrency));
    }
}

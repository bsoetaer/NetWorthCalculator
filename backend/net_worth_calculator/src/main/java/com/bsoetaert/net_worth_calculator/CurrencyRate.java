package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class CurrencyRate {
    private String targetCurrency;
    private LocalDate lastUpdated;
    private Map<String, BigDecimal> conversionRates;

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Map<String, BigDecimal> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(Map<String, BigDecimal> conversionRates) {
        this.conversionRates = conversionRates;
    }

    public Boolean canConvert(String newCurrency) {
        return conversionRates.containsKey(newCurrency);
    }

    public BigDecimal convertFrom(BigDecimal value, String newCurrency) {
        return value.multiply(conversionRates.get(newCurrency));
    }

    public CurrencyRate(String targetCurrency) {
        this.targetCurrency = targetCurrency;
        this.lastUpdated = LocalDate.now();
        this.conversionRates = new HashMap<String, BigDecimal>();
        this.conversionRates.put(targetCurrency, new BigDecimal(1));
    }

    public void addStartCurrency(String startingCurrency, BigDecimal exchangeRate) {
        this.conversionRates.put(startingCurrency, exchangeRate);
    }
}
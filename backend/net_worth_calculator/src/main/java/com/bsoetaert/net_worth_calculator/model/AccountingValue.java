package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public class AccountingValue {
    private String baseCurrency;
    private String name;
    private BigDecimal baseValue;
    private BigDecimal value;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(BigDecimal baseValue) {
        this.baseValue = baseValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    // Needed for JSON convert
    public AccountingValue() {}

    public AccountingValue(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
    }
}

package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "value",
        "baseValue",
        "baseCurrency"
})
public class AccountingValue {

    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private BigDecimal value;
    @JsonIgnore
    private BigDecimal baseValue;
    @JsonIgnore
    private String baseCurrency;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("value")
    public BigDecimal getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @JsonIgnore
    public BigDecimal getBaseValue() {
        return baseValue;
    }

    @JsonProperty("baseValue")
    public void setBaseValue(BigDecimal baseValue) {
        this.baseValue = baseValue;
    }

    @JsonIgnore
    public String getBaseCurrency() {
        return baseCurrency;
    }

    @JsonProperty("baseCurrency")
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

}
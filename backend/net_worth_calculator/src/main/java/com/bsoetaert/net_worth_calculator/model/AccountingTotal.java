package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "assets",
        "liabilities",
        "netWorth"
})
public class AccountingTotal {

    @JsonProperty("assets")
    private BigDecimal assets;
    @JsonProperty("liabilities")
    private BigDecimal liabilities;
    @JsonProperty("netWorth")
    private BigDecimal netWorth;

    @JsonProperty("assets")
    public BigDecimal getAssets() {
        return assets;
    }

    @JsonProperty("assets")
    public void setAssets(BigDecimal assets) {
        this.assets = assets;
    }

    @JsonProperty("liabilities")
    public BigDecimal getLiabilities() {
        return liabilities;
    }

    @JsonProperty("liabilities")
    public void setLiabilities(BigDecimal liabilities) {
        this.liabilities = liabilities;
    }

    @JsonProperty("netWorth")
    public BigDecimal getNetWorth() {
        return netWorth;
    }

    @JsonProperty("netWorth")
    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

}
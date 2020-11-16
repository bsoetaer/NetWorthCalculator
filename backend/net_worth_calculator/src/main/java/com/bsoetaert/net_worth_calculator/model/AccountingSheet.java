package com.bsoetaert.net_worth_calculator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "totals",
        "assets",
        "liabilities",
        "categories"
})
public class AccountingSheet {

    @JsonProperty("totals")
    private AccountingTotal totals;
    @JsonProperty("assets")
    private List<AccountingItem> assets = null;
    @JsonProperty("liabilities")
    private List<AccountingItem> liabilities = null;
    @JsonIgnore
    private List<AccountingCategory> categories = null;

    @JsonProperty("totals")
    public AccountingTotal getTotals() {
        return totals;
    }

    @JsonProperty("totals")
    public void setTotals(AccountingTotal totals) {
        this.totals = totals;
    }

    @JsonProperty("assets")
    public List<AccountingItem> getAssets() {
        return assets;
    }

    @JsonProperty("assets")
    public void setAssets(List<AccountingItem> assets) {
        this.assets = assets;
    }

    @JsonProperty("liabilities")
    public List<AccountingItem> getLiabilities() {
        return liabilities;
    }

    @JsonProperty("liabilities")
    public void setLiabilities(List<AccountingItem> liabilities) {
        this.liabilities = liabilities;
    }

    @JsonIgnore
    public List<AccountingCategory> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<AccountingCategory> categories) {
        this.categories = categories;
    }

    public AccountingItem getAsset(Integer id) {
        for (AccountingItem item : assets) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public AccountingItem getLiability(Integer id) {
        for (AccountingItem item : liabilities) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

}
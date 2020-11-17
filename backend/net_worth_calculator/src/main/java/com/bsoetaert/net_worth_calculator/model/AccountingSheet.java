package com.bsoetaert.net_worth_calculator.model;

import java.util.List;

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

    public AccountingItem getItem(Integer id) {
        AccountingItem item = findItem(id, assets);

        if(item == null) {
            item  = findItem(id, liabilities);
        }

        return item;
    }

    private AccountingItem findItem(Integer id, List<AccountingItem> items) {
        for (AccountingItem item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
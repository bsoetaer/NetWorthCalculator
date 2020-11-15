package com.bsoetaert.net_worth_calculator.model;

import java.math.BigDecimal;

public class Accounting {
    private BigDecimal total;
    private AccountingGroup assets;
    private AccountingGroup liabilities;
    private Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public AccountingGroup getAssets() {
        return assets;
    }

    public void setAssets(AccountingGroup assets) {
        this.assets = assets;
    }

    public AccountingGroup getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(AccountingGroup liabilities) {
        this.liabilities = liabilities;
    }
}

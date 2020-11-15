package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class AccountingGroup {
    private BigDecimal total;
    private AccountingCategory[] items;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public AccountingCategory[] getItems() {
        return items;
    }

    public void setItems(AccountingCategory[] items) {
        this.items = items;
    }
}

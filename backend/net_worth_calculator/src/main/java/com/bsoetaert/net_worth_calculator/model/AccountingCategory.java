package com.bsoetaert.net_worth_calculator.model;

public class AccountingCategory {
    private String category;
    private AccountingItem[] values;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public AccountingItem[] getValues() {
        return values;
    }

    public void setValues(AccountingItem[] values) {
        this.values = values;
    }
}

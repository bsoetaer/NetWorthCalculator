package com.bsoetaert.net_worth_calculator.model;

public class AccountingItem {
    private String name;
    private AccountingValue[] values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountingValue[] getValues() {
        return values;
    }

    public void setValues(AccountingValue[] values) {
        this.values = values;
    }
}

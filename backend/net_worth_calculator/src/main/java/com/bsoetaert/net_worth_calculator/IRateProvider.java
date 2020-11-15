package com.bsoetaert.net_worth_calculator;

public interface IRateProvider {
    public String getName();

    public CurrencyRate getRates(String baseCurrency);
}

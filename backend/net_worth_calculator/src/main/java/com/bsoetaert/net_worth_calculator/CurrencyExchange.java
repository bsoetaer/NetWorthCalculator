package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.Currency;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CurrencyExchange {
    static Logger logger = LoggerFactory.getLogger(CurrencyExchange.class);

    public static Boolean canConvert(String baseCurrency, String newCurrency) {
        if(baseCurrency.equals(newCurrency))
            return true;

        Map<String, CurrencyRate> rates = DataStore.getExchangeRates();
        return (rates.containsKey(newCurrency) && rates.get(newCurrency).canConvert(baseCurrency));
    }

    public static BigDecimal convert(BigDecimal value, String baseCurrency, String newCurrency) {
        if(baseCurrency.equals(newCurrency))
            return value;
        Map<String, CurrencyRate> rates = DataStore.getExchangeRates();
        return rates.get(newCurrency).convertFrom(value, baseCurrency);
    }

    public static void updateRates(String baseCurrency, Collection<IRateProvider> rateProviders) {
        LocalDate currentDate = LocalDate.now();
        Map<String, CurrencyRate> rates = DataStore.getExchangeRates();
        if(rates.containsKey(baseCurrency) && rates.get(baseCurrency).getLastUpdated().compareTo(currentDate) == 0)
        {
            return;
        }

        CurrencyRate newRate = null;
        for(IRateProvider rateProvider : rateProviders) {
            newRate = rateProvider.getRates(baseCurrency);
            if(newRate != null) {
                break;
            }
            logger.warn(rateProvider.getName() + " failed to retrieve rates for " + baseCurrency + ".");
        }

        if(newRate == null)
        {
            if(!rates.containsKey(baseCurrency))
            {
                String message = "Failed to get exchange rates for currency: " + baseCurrency;
                logger.error(message);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
            }
            else
            {
                logger.warn("Unable to update rates for currency: " + baseCurrency + ". " +
                        "Using old rates from " + rates.get(baseCurrency).getLastUpdated().toString());
            }
        }
        else {
            newRate.setTargetCurrency(baseCurrency);
            newRate.setLastUpdated(LocalDate.now());
            rates.put(baseCurrency, newRate);
        }
    }
}

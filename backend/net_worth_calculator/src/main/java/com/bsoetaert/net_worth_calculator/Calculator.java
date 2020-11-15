package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.*;
import com.bsoetaert.net_worth_calculator.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class Calculator {

    Logger logger = LoggerFactory.getLogger(Calculator.class);

    private static Map<String, CurrencyRate> storedRates = new HashMap<>();
    private CurrencyExchange exchange;

    @PostMapping(value = "/calculate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Accounting calculateTotals(@RequestBody Accounting accounting) {
        // TODO Testing all non-models
        Collection<IRateProvider> rateProviders = new ArrayList<IRateProvider>();
        rateProviders.add(new ExchangeRatesApiProvider());
        return calculateTotals(accounting, rateProviders);
    }

    public Accounting calculateTotals(Accounting accounting, Collection<IRateProvider> rateProviders) {
        exchange = new CurrencyExchange(rateProviders, storedRates);
        String newCurrency = accounting.getCurrency().getName();
        exchange.updateRates(newCurrency);
        storedRates = exchange.getRates();

        // TODO Consider better error messages than throwing exceptions when request json doesn't match models
        accounting.setAssets(convertAndSumValues(accounting.getAssets(), newCurrency));
        accounting.setLiabilities(convertAndSumValues(accounting.getLiabilities(), newCurrency));
        accounting.setTotal(accounting.getAssets().getTotal().subtract(accounting.getLiabilities().getTotal()));

        return accounting;
    }

    private AccountingGroup convertAndSumValues(AccountingGroup acctGroup, String newCurrency) {
        BigDecimal total = new BigDecimal(0);
        for(AccountingCategory cat : acctGroup.getItems())
        {
            for(AccountingItem item : cat.getValues())
            {
                for(AccountingValue value : item.getValues())
                {
                    String baseCurrency = value.getBaseCurrency();
                    if( !exchange.canConvert(baseCurrency, newCurrency ) ) {
                        String message = "Converting from " + baseCurrency + " is  not supported.";
                        logger.error(message);
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
                    }
                    value.setValue(exchange.convert(value.getBaseValue(), baseCurrency, newCurrency).setScale(2, RoundingMode.HALF_UP));

                    if(value.getName().equals("value")) {
                        total = total.add(value.getValue());
                    }
                }
            }
        }
        acctGroup.setTotal(total.setScale(2, RoundingMode.HALF_UP));

        return acctGroup;
    }
}

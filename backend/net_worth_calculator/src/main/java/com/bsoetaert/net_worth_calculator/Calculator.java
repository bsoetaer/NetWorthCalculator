package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Calculator {
    Logger logger = LoggerFactory.getLogger(Calculator.class);
    AccountingSheet sheet;
    String newCurrency;
    CurrencyExchange exchange;

    public Calculator(AccountingSheet sheet, String newCurrency, CurrencyExchange exchange) {
        this.sheet = sheet;
        this.newCurrency = newCurrency;
        this.exchange = exchange;
    }

    public AccountingSheet calculate()
    {
        exchange.updateRates(newCurrency);
        BigDecimal total = new BigDecimal(0);
        BigDecimal assetTotal = total(sheet.getAssets());
        BigDecimal liabilityTotal = total(sheet.getLiabilities());

        total = assetTotal.subtract(liabilityTotal);
        sheet.getTotals().setAssets(assetTotal);
        sheet.getTotals().setLiabilities(liabilityTotal);
        sheet.getTotals().setNetWorth(total);

        return sheet;
    }

    private BigDecimal total(List<AccountingItem> items) {
        BigDecimal total = new BigDecimal(0);
        for(AccountingItem item : items)
        {
            for(AccountingValue value : item.getValues())
            {
                String baseCurrency = value.getBaseCurrency();
                if( !exchange.canConvert(baseCurrency, newCurrency) ) {
                    String message = "Converting from " + baseCurrency + " to " + newCurrency + " is  not supported.";
                    logger.error(message);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
                }
                value.setValue(exchange.convert(value.getBaseValue(), baseCurrency, newCurrency).setScale(2, RoundingMode.HALF_UP));

                if(value.getName().equals("value")) {
                    total = total.add(value.getValue());
                }
            }
        }
        return total;
    }
}

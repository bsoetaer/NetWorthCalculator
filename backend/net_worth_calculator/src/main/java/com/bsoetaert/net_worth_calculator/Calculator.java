package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.*;
import com.bsoetaert.net_worth_calculator.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class Calculator {
    Logger logger = LoggerFactory.getLogger(Calculator.class);
    private Collection<IRateProvider> rateProviders;

    public void setRateProviders(Collection<IRateProvider> rateProviders) {
        this.rateProviders = rateProviders;
    }

    public Calculator() {
        rateProviders = new ArrayList<IRateProvider>();
        rateProviders.add(new ExchangeRatesApiProvider());
    }

    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountingSheet convert(
            @RequestParam Integer userId,
            @RequestParam String currency
    ) {
        validateUserId(userId);
        validateCurrency(currency);

        AccountingSheet sheet = DataStore.getSheet(userId);

        CurrencyExchange.updateRates(currency, rateProviders);
        calculate(sheet, currency);
        return sheet;
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountingCategory> getCategories(@RequestParam Integer userId) {
        validateUserId(userId);
        return DataStore.getSheet(userId).getCategories();
    }

    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser() {
        Integer userId = DataStore.addUser();
        User user = new User();
        user.setUserId(userId);
        return user;
    }

    @PutMapping(value = "/value", produces =  MediaType.APPLICATION_JSON_VALUE)
    public AccountingTotal updateItem(
            @RequestBody ValueUpdate valueUpdate,
            @RequestParam Integer id,
            @RequestParam Integer userId
    )
    {
        if(id == null || valueUpdate.getId() != null && valueUpdate.getId() != id)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Query Parameter for id does not match body id.");
        }

        validateUserId(userId);
        validateCurrency(valueUpdate.getCurrency());


        if(valueUpdate.getValue() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Value element must be a valid currency amount.");
        }

        AccountingSheet sheet = DataStore.getSheet(userId);
        AccountingItem itemToUpdate = sheet.getAsset(id);

        if(itemToUpdate == null) {
            itemToUpdate = sheet.getLiability(id);
        }

        if(itemToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No asset exists with the given id.");
        }

        AccountingValue valueToUpdate = itemToUpdate.getValue(valueUpdate.getName());

        if(valueToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No value exists with the given name.");
        }

        valueToUpdate.setBaseValue(valueUpdate.getValue());
        valueToUpdate.setBaseCurrency(valueUpdate.getCurrency());

        CurrencyExchange.updateRates(valueUpdate.getCurrency(), rateProviders);
        return calculate(sheet, valueUpdate.getCurrency());
    }

    private void validateUserId(Integer userId) {
        if(userId == null || !DataStore.hasUserSession(userId))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User session does not exist. Call /user first.");
        }
    }

    private void validateCurrency(String currency) {
        if(currency == null || currency.length() != 3)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency element must be a 3 character identifier.");
        }
    }

    private AccountingTotal calculate(AccountingSheet sheet, String newCurrency)
    {
        BigDecimal total = new BigDecimal(0);
        BigDecimal assetTotal = total(sheet.getAssets(), newCurrency);
        BigDecimal liabilityTotal = total(sheet.getLiabilities(), newCurrency);

        total = assetTotal.subtract(liabilityTotal);
        sheet.getTotals().setAssets(assetTotal);
        sheet.getTotals().setLiabilities(liabilityTotal);
        sheet.getTotals().setNetWorth(total);

        return sheet.getTotals();
    }

    private BigDecimal total(List<AccountingItem> items, String newCurrency) {
        BigDecimal total = new BigDecimal(0);
        for(AccountingItem item : items)
        {
            for(AccountingValue value : item.getValues())
            {
                String baseCurrency = value.getBaseCurrency();
                if( !CurrencyExchange.canConvert(baseCurrency, newCurrency ) ) {
                    String message = "Converting from " + baseCurrency + " to " + newCurrency + " is  not supported.";
                    logger.error(message);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
                }
                value.setValue(CurrencyExchange.convert(value.getBaseValue(), baseCurrency, newCurrency).setScale(2, RoundingMode.HALF_UP));

                if(value.getName().equals("value")) {
                    total = total.add(value.getValue());
                }
            }
        }
        return total;
    }

    @PostMapping(value = "/calculate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Accounting calculateTotals(@RequestBody Accounting accounting) {
        // TODO Testing all non-models
        Collection<IRateProvider> rateProviders = new ArrayList<IRateProvider>();
        rateProviders.add(new ExchangeRatesApiProvider());
        return accounting;
//        return calculateTotals(accounting, rateProviders);
    }

//    public Accounting calculateTotals(Accounting accounting, Collection<IRateProvider> rateProviders) {
//        exchange = new CurrencyExchange(rateProviders, storedRates);
//        String newCurrency = accounting.getCurrency().getName();
//        exchange.updateRates(newCurrency);
//        storedRates = exchange.getRates();
//
//        // TODO Consider better error messages than throwing exceptions when request json doesn't match models
//        accounting.setAssets(convertAndSumValues(accounting.getAssets(), newCurrency));
//        accounting.setLiabilities(convertAndSumValues(accounting.getLiabilities(), newCurrency));
//        accounting.setTotal(accounting.getAssets().getTotal().subtract(accounting.getLiabilities().getTotal()));
//
//        return accounting;
//    }
//
//    private AccountingGroup convertAndSumValues(AccountingGroup acctGroup, String newCurrency) {
//        BigDecimal total = new BigDecimal(0);
//        for(AccountingCategory cat : acctGroup.getItems())
//        {
//            for(AccountingItem item : cat.getValues())
//            {
//                for(AccountingValue value : item.getValues())
//                {
//                    String baseCurrency = value.getBaseCurrency();
//                    if( !exchange.canConvert(baseCurrency, newCurrency ) ) {
//                        String message = "Converting from " + baseCurrency + " is  not supported.";
//                        logger.error(message);
//                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
//                    }
//                    value.setValue(exchange.convert(value.getBaseValue(), baseCurrency, newCurrency).setScale(2, RoundingMode.HALF_UP));
//
//                    if(value.getName().equals("value")) {
//                        total = total.add(value.getValue());
//                    }
//                }
//            }
//        }
//        acctGroup.setTotal(total.setScale(2, RoundingMode.HALF_UP));
//
//        return acctGroup;
//    }
}

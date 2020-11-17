package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
public class ValueController {

    @PutMapping(value = "/value", produces =  MediaType.APPLICATION_JSON_VALUE)
    public AccountingTotal updateItem(
            @RequestBody ValueUpdate valueUpdate,
            @RequestParam Integer id,
            @RequestParam Integer userId
    )
    {
        Validator.validateUserId(userId);
        Validator.validateCurrency(valueUpdate.getCurrency());
        validateId(id, valueUpdate.getId());
        validateValue(valueUpdate.getValue());

        AccountingSheet sheet = updateValue(id, userId, valueUpdate);

        Calculator calc = new Calculator(sheet, valueUpdate.getCurrency(), DataStore.getExchange());
        sheet = calc.calculate();
        DataStore.setSheet(userId, sheet);
        return sheet.getTotals();
    }

    private void validateId(Integer queryId, Integer requestId) {
        if(queryId == null || requestId != null && requestId != queryId)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Query Parameter for id does not match body id.");
        }
    }

    private void validateValue(BigDecimal value) {
        if(value == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Value element must be a valid currency amount.");
        }
    }

    private AccountingSheet updateValue(Integer id, Integer userId, ValueUpdate valueUpdate) {
        AccountingSheet sheet = DataStore.getSheet(userId);
        AccountingItem itemToUpdate = sheet.getItem(id);

        if(itemToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No asset exists with the given id.");
        }

        AccountingValue valueToUpdate = itemToUpdate.getValue(valueUpdate.getName());

        if(valueToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No value exists with the given name.");
        }

        valueToUpdate.setBaseValue(valueUpdate.getValue());
        valueToUpdate.setBaseCurrency(valueUpdate.getCurrency());

        return sheet;
    }
}

package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.AccountingSheet;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConvertController {

    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountingSheet convert(
            @RequestParam Integer userId,
            @RequestParam String currency
    ) {
        Validator.validateUserId(userId);
        Validator.validateCurrency(currency);

        AccountingSheet sheet = DataStore.getSheet(userId);

        Calculator calc = new Calculator(sheet, currency, DataStore.getExchange());
        sheet = calc.calculate();
        DataStore.setSheet(userId, sheet);
        return sheet;
    }
}

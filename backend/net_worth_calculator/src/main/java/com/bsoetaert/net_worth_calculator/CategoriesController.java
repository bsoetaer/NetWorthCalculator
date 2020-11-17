package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.AccountingCategory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoriesController {
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountingCategory> getCategories(@RequestParam Integer userId) {
        Validator.validateUserId(userId);
        return DataStore.getSheet(userId).getCategories();
    }
}

package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class CalculatorTests {
    @Test
    public void largePositiveTotal() {
    }

    @Test
    public void negativeNetWorth() {
    }

    @Test
    public void invalidBaseCurrency() {
    }

    @Test
    public void zeroLiabilities() {
    }

    @Test
    public void zeroAssets() {
    }

    @Test
    public void zeroLiabilitiesAndZeroAssets() {
    }

    private AccountingSheet createBasicSheet() {
        AccountingSheet sheet = new AccountingSheet();
        sheet.setTotals(createBasicTotal());
        sheet.setLiabilities(createBasicItemList(1, "asset", false));
        sheet.setAssets(createBasicItemList(3, "liability", true));
        return sheet;
    }

    private AccountingTotal createBasicTotal() {
        AccountingTotal total = new AccountingTotal();
        total.setNetWorth(new BigDecimal("0.0"));
        total.setLiabilities(new BigDecimal("0.0"));
        total.setAssets(new BigDecimal("0.0"));
        return total;
    }

    private List<AccountingItem> createBasicItemList(Integer startId, String namePrefix, boolean includePayment) {
        List<AccountingItem> items = new ArrayList<>();

        items.add(createBasicItem(startId++, namePrefix + "1", includePayment));
        items.add(createBasicItem(startId++, namePrefix + "2", includePayment));

        return items;
    }

    private AccountingItem createBasicItem(Integer id, String name, boolean includePayment) {
        AccountingItem item = new AccountingItem();
        List<AccountingValue> values = new ArrayList<>();
        values.add(createBasicValue("value"));

        if(includePayment)
        {
            values.add(createBasicValue("payment"));
        }

        item.setId(id);
        item.setName(name);
        item.setValues(values);
        return item;
    }

    private AccountingValue createBasicValue(String name) {
        AccountingValue acctValue = new AccountingValue();
        acctValue.setName(name);
        acctValue.setBaseCurrency("CAD");
        acctValue.setBaseValue(new BigDecimal("0.0"));

        return acctValue;
    }
}

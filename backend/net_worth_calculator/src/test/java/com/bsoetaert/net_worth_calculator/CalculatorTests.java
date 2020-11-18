package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class CalculatorTests {
    @Test
    public void largePositiveTotal() {
        AccountingSheet sheet = createBasicSheet();
        BigDecimal expectedTotal = new BigDecimal(0).setScale(2);

        for(AccountingItem item : sheet.getAssets())
        {
            BigDecimal maxDouble = new BigDecimal(Double.MAX_VALUE);
            item.getValue("value").setBaseValue(maxDouble);
            expectedTotal = expectedTotal.add(maxDouble).setScale(2);
        }

        Calculator calc = new Calculator(sheet, "CAD", createBasicExchange());

        AccountingSheet updatedSheet = calc.calculate();

        assertEquals(expectedTotal, updatedSheet.getTotals().getAssets());
        assertEquals(new BigDecimal(0).setScale(2), updatedSheet.getTotals().getLiabilities());
        assertEquals(expectedTotal, updatedSheet.getTotals().getNetWorth());
    }

    @Test
    public void largeNegativeTotal() {
        AccountingSheet sheet = createBasicSheet();
        BigDecimal expectedTotal = new BigDecimal(0).setScale(2);

        for(AccountingItem item : sheet.getLiabilities())
        {
            BigDecimal maxDouble = new BigDecimal(Double.MAX_VALUE);
            item.getValue("value").setBaseValue(maxDouble);
            expectedTotal = expectedTotal.subtract(maxDouble).setScale(2);
        }

        Calculator calc = new Calculator(sheet, "CAD", createBasicExchange());

        AccountingSheet updatedSheet = calc.calculate();

        assertEquals(new BigDecimal(0).setScale(2), updatedSheet.getTotals().getAssets());
        assertEquals(expectedTotal.negate(), updatedSheet.getTotals().getLiabilities());
        assertEquals(expectedTotal, updatedSheet.getTotals().getNetWorth());
    }

    @Test
    public void invalidBaseCurrency() {
        AccountingSheet sheet = createBasicSheet();
        sheet.getAssets().get(0).getValue("value").setBaseCurrency("UNKNOWN");

        Calculator calc = new Calculator(sheet, "CAD", createBasicExchange());

        assertThrows(ResponseStatusException.class, calc::calculate);
    }

    @Test
    public void zeroLiabilities() {
        AccountingSheet sheet = createBasicSheet();
        BigDecimal expectedTotal = new BigDecimal(0).setScale(2);

        for(AccountingItem item : sheet.getAssets())
        {
            BigDecimal newValue = new BigDecimal(1);
            item.getValue("value").setBaseValue(newValue);
            expectedTotal = expectedTotal.add(newValue).setScale(2);
        }

        sheet.setLiabilities(new ArrayList<>());

        Calculator calc = new Calculator(sheet, "CAD", createBasicExchange());

        AccountingSheet updatedSheet = calc.calculate();

        assertEquals(expectedTotal, updatedSheet.getTotals().getAssets());
        assertEquals(new BigDecimal(0).setScale(2), updatedSheet.getTotals().getLiabilities());
        assertEquals(expectedTotal, updatedSheet.getTotals().getNetWorth());
    }

    @Test
    public void zeroAssets() {
        AccountingSheet sheet = createBasicSheet();
        BigDecimal expectedTotal = new BigDecimal(0).setScale(2);

        for(AccountingItem item : sheet.getLiabilities())
        {
            BigDecimal newValue = new BigDecimal(1);
            item.getValue("value").setBaseValue(newValue);
            expectedTotal = expectedTotal.add(newValue).setScale(2);
        }

        sheet.setAssets(new ArrayList<>());

        Calculator calc = new Calculator(sheet, "CAD", createBasicExchange());

        AccountingSheet updatedSheet = calc.calculate();

        assertEquals(new BigDecimal(0).setScale(2), updatedSheet.getTotals().getAssets());
        assertEquals(expectedTotal, updatedSheet.getTotals().getLiabilities());
        assertEquals(expectedTotal.negate(), updatedSheet.getTotals().getNetWorth());
    }

    @Test
    public void zeroLiabilitiesAndZeroAssets() {
        AccountingSheet sheet = createBasicSheet();
        BigDecimal expectedTotal = new BigDecimal(0).setScale(2);

        sheet.setAssets(new ArrayList<>());
        sheet.setLiabilities(new ArrayList<>());

        Calculator calc = new Calculator(sheet, "CAD", createBasicExchange());

        AccountingSheet updatedSheet = calc.calculate();

        assertEquals(expectedTotal, updatedSheet.getTotals().getAssets());
        assertEquals(expectedTotal, updatedSheet.getTotals().getLiabilities());
        assertEquals(expectedTotal, updatedSheet.getTotals().getNetWorth());
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

    private CurrencyExchange createBasicExchange() {
        String targetCurrency = "CAD";
        String baseCurrency = "USD";

        BigDecimal exchangeRate1 = new BigDecimal(String.valueOf(1.5));
        CurrencyRate rate1 = new CurrencyRate(targetCurrency);
        rate1.addStartCurrency(baseCurrency, exchangeRate1);

        IRateProvider provider1 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider1.getName()).thenReturn("Mock1");
        Mockito.when(provider1.getRates(anyString())).thenReturn(rate1);
        ArrayList<IRateProvider> providerList1 = new ArrayList<>();
        providerList1.add(provider1);

        return new CurrencyExchange(providerList1);
    }
}

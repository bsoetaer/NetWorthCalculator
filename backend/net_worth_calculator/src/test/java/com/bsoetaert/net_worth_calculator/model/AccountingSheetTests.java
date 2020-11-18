package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountingSheetTests {
    @Test
    public void serialization() throws Exception {
        String jsonString =
                "{\"totals\":{}," +
                        "\"assets\":[{}]," +
                        "\"liabilities\":[{},{}]," +
                        "\"categories\":[{},{},{}]}";

        ObjectMapper mapper = new ObjectMapper();
        AccountingSheet readValue = mapper.readValue(jsonString, AccountingSheet.class);

        assertNotNull(readValue.getTotals());
        assertEquals(1, readValue.getAssets().size());
        assertEquals(2, readValue.getLiabilities().size());
        assertEquals(3, readValue.getCategories().size());

        String serialized = mapper.writeValueAsString(readValue);
        assertTrue(serialized.contains("totals"));
        assertTrue(serialized.contains("assets"));
        assertTrue(serialized.contains("liabilities"));
        assertFalse(serialized.contains("categories"));
    }

    @Test
    public void getItemBothSet() {
        AccountingSheet sheet = new AccountingSheet();

        AccountingItem item1 = new AccountingItem();
        item1.setId(1);
        AccountingItem item2 = new AccountingItem();
        item2.setId(2);
        AccountingItem item3 = new AccountingItem();
        item3.setId(3);
        AccountingItem item4 = new AccountingItem();
        item4.setId(4);
        AccountingItem item5 = new AccountingItem(); // null id

        List<AccountingItem> assetItems = new ArrayList<>();
        assetItems.add(item1);
        assetItems.add(item2);
        assetItems.add(item5);

        List<AccountingItem> liabilityItems = new ArrayList<>();
        liabilityItems.add(item3);
        liabilityItems.add(item4);
        liabilityItems.add(item5);

        sheet.setAssets(assetItems);
        sheet.setLiabilities(liabilityItems);

        assertEquals(item1, sheet.getItem(1));
        assertEquals(item2, sheet.getItem(2));
        assertEquals(item3, sheet.getItem(3));
        assertEquals(item4, sheet.getItem(4));
        assertEquals(null, sheet.getItem(5));
        assertEquals(null, sheet.getItem(-1));
    }

    @Test
    public void getItemNoAssets() {
        AccountingSheet sheet = new AccountingSheet();
        AccountingItem item3 = new AccountingItem();
        item3.setId(3);
        AccountingItem item4 = new AccountingItem();
        item4.setId(4);
        AccountingItem item5 = new AccountingItem(); // null id

        List<AccountingItem> liabilityItems = new ArrayList<>();
        liabilityItems.add(item3);
        liabilityItems.add(item4);
        liabilityItems.add(item5);

        sheet.setLiabilities(liabilityItems);

        assertEquals(item3, sheet.getItem(3));
        assertEquals(item4, sheet.getItem(4));
        assertEquals(null, sheet.getItem(5));
        assertEquals(null, sheet.getItem(-1));
    }

    @Test
    public void getItemNoLiabilities() {
        AccountingSheet sheet = new AccountingSheet();

        AccountingItem item1 = new AccountingItem();
        item1.setId(1);
        AccountingItem item2 = new AccountingItem();
        item2.setId(2);
        AccountingItem item5 = new AccountingItem(); // null id

        List<AccountingItem> assetItems = new ArrayList<>();
        assetItems.add(item1);
        assetItems.add(item2);
        assetItems.add(item5);

        sheet.setAssets(assetItems);

        assertEquals(item1, sheet.getItem(1));
        assertEquals(item2, sheet.getItem(2));
        assertEquals(null, sheet.getItem(5));
        assertEquals(null, sheet.getItem(-1));
    }
}

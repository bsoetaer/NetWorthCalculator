package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AccountingItemTests {
    @Test
    public void serialization() throws Exception {
        String jsonString =
                "{\"name\":\"a\"," +
                    "\"id\":1," +
                    "\"category\":2," +
                    "\"values\":[{}]}";

        ObjectMapper mapper = new ObjectMapper();
        AccountingItem readValue = mapper.readValue(jsonString, AccountingItem.class);

        assertEquals("a", readValue.getName());
        assertEquals(1, readValue.getId());
        assertEquals(2, readValue.getCategory());
        assertEquals(1, readValue.getValues().size());

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("name"));
        assertTrue(deserialized.contains("id"));
        assertTrue(deserialized.contains("category"));
        assertTrue(deserialized.contains("values"));
    }

    @Test
    public void getValue() {
        AccountingItem item = new AccountingItem();

        AccountingValue value1 = new AccountingValue();
        value1.setName("value");
        AccountingValue value2 = new AccountingValue();
        value2.setName("other");
        AccountingValue value3 = new AccountingValue(); // null name

        List<AccountingValue> values = new ArrayList<>();
        values.add(value1);
        values.add(value2);
        values.add(value3);

        item.setValues(values);

        assertEquals(value1, item.getValue("value"));
        assertEquals(value2, item.getValue("other"));
        assertEquals(null, item.getValue(null));
        assertEquals(null, item.getValue("nothing"));
    }
}

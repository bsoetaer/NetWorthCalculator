package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountingValueTests {
    @Test
    public void success() throws Exception {
        String jsonString =
                "{\"name\":\"a\"," +
                        "\"baseCurrency\":\"CAD\"," +
                        "\"baseValue\":5.5," +
                        "\"value\":5.5}";

        ObjectMapper mapper = new ObjectMapper();
        AccountingValue readValue = mapper.readValue(jsonString, AccountingValue.class);

        assertEquals("a", readValue.getName());
        assertEquals("CAD", readValue.getBaseCurrency());
        assertEquals(new BigDecimal(String.valueOf(5.5)), readValue.getBaseValue());
        assertEquals(new BigDecimal(String.valueOf(5.5)), readValue.getValue());

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("name"));
        assertTrue(deserialized.contains("value"));
        assertFalse(deserialized.contains("baseCurrency"));
        assertFalse(deserialized.contains("baseValue"));
    }
}

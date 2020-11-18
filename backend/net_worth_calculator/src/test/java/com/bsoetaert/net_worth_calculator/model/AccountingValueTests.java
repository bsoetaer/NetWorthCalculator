package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountingValueTests {
    @Test
    public void serialization() throws Exception {
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

        String serialized = mapper.writeValueAsString(readValue);
        assertTrue(serialized.contains("name"));
        assertTrue(serialized.contains("value"));
        assertFalse(serialized.contains("baseCurrency"));
        assertFalse(serialized.contains("baseValue"));
    }
}

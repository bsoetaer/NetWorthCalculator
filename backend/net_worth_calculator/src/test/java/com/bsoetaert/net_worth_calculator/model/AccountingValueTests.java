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

        assertEquals(readValue.getName(), "a");
        assertEquals(readValue.getBaseCurrency(), "CAD");
        assertEquals(readValue.getBaseValue(), new BigDecimal(String.valueOf(5.5)));
        assertEquals(readValue.getValue(), new BigDecimal(String.valueOf(5.5)));

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("name"));
        assertTrue(deserialized.contains("baseCurrency"));
        assertTrue(deserialized.contains("baseValue"));
        assertTrue(deserialized.contains("value"));
    }

    @Test
    public void missingFields() {
        String jsonString =
                "{}";

        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> { mapper.readValue(jsonString, AccountingValue.class); });
    }
}

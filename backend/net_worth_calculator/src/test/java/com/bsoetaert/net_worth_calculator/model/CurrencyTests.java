package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CurrencyTests {
    @Test
    public void success() throws Exception {
        String jsonString =
                "{\"name\":\"a\"," +
                    "\"symbol\":\"b\"}";

        ObjectMapper mapper = new ObjectMapper();
        Currency readValue = mapper.readValue(jsonString, Currency.class);

        assertEquals(readValue.getName(), "a");
        assertEquals(readValue.getSymbol(), "b");

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("name"));
        assertTrue(deserialized.contains("symbol"));
    }

    @Test
    public void missingFields() {
        String jsonString =
                "{}";

        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> { mapper.readValue(jsonString, Currency.class); });
    }
}

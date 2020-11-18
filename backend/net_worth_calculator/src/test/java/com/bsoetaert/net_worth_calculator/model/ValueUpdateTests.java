package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValueUpdateTests {
    @Test
    public void serialization() throws Exception {
        String jsonString =
                "{\"name\":\"a\"," +
                        "\"currency\":\"CAD\"," +
                        "\"value\":2.5," +
                        "\"id\":1}";

        ObjectMapper mapper = new ObjectMapper();
        ValueUpdate readValue = mapper.readValue(jsonString, ValueUpdate.class);

        assertEquals("a", readValue.getName());
        assertEquals("CAD", readValue.getCurrency());
        assertEquals(new BigDecimal(String.valueOf(2.5)), readValue.getValue());
        assertEquals(1, readValue.getId());

        String serialized = mapper.writeValueAsString(readValue);
        assertTrue(serialized.contains("name"));
        assertTrue(serialized.contains("currency"));
        assertTrue(serialized.contains("value"));
        assertTrue(serialized.contains("id"));
    }
}

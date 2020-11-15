package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AccountingTests {
    @Test
    public void success() throws Exception {
        String jsonString =
                "{\"total\":1," +
                        "\"assets\":{\"total\":2}," +
                        "\"liabilities\":{\"total\":3}," +
                        "\"currency\":{\"name\":\"CAD\"}}";

        ObjectMapper mapper = new ObjectMapper();
        Accounting readValue = mapper.readValue(jsonString, Accounting.class);

        assertEquals(readValue.getTotal(), new BigDecimal(1));
        assertEquals(readValue.getAssets().getTotal(), new BigDecimal(2));
        assertEquals(readValue.getLiabilities().getTotal(), new BigDecimal(3));
        assertEquals(readValue.getCurrency().getName(), "CAD");

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("total"));
        assertTrue(deserialized.contains("assets"));
        assertTrue(deserialized.contains("liabilities"));
    }

    @Test
    public void missingFields() {
        String jsonString =
                "{}";

        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> { mapper.readValue(jsonString, Accounting.class); });
    }
}

package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AccountingGroupTests {
    @Test
    public void success() throws Exception {
        String jsonString =
                "{\"total\":1," +
                        "\"items\":[{}]}";

        ObjectMapper mapper = new ObjectMapper();
        AccountingGroup readValue = mapper.readValue(jsonString, AccountingGroup.class);

        assertEquals(readValue.getTotal(), new BigDecimal(1));
        assertEquals(readValue.getItems().length, 1);

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("total"));
        assertTrue(deserialized.contains("items"));
    }

    @Test
    public void missingFields() {
        String jsonString =
                "{}";

        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> { mapper.readValue(jsonString, AccountingGroup.class); });
    }
}

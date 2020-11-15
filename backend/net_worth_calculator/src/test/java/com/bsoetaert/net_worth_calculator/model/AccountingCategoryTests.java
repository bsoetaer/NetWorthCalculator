package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountingCategoryTests {
    @Test
    public void success() throws Exception {
        String jsonString =
                "{\"category\":\"a\"," +
                        "\"values\":[{}]}";

        ObjectMapper mapper = new ObjectMapper();
        AccountingCategory readValue = mapper.readValue(jsonString, AccountingCategory.class);

        assertEquals(readValue.getCategory(), "a");
        assertEquals(readValue.getValues().length, 1);

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("category"));
        assertTrue(deserialized.contains("values"));
    }

    @Test
    public void missingFields() {
        String jsonString =
                "{}";

        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> { mapper.readValue(jsonString, AccountingCategory.class); });
    }
}

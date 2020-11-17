package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AccountingItemTests {
    @Test
    public void success() throws Exception {
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
}

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
                        "\"values\":[{}]}";

        ObjectMapper mapper = new ObjectMapper();
        AccountingItem readValue = mapper.readValue(jsonString, AccountingItem.class);

        assertEquals(readValue.getName(), "a");
        //assertEquals(readValue.getValues().length, 1);

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("name"));
        assertTrue(deserialized.contains("values"));
    }

    @Test
    public void missingFields() {
        String jsonString =
                "{}";

        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> { mapper.readValue(jsonString, AccountingItem.class); });
    }
}

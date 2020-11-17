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
                "{\"name\":\"a\"," +
                        "\"id\":1}";

        ObjectMapper mapper = new ObjectMapper();
        AccountingCategory readValue = mapper.readValue(jsonString, AccountingCategory.class);

        assertEquals("a", readValue.getName());
        assertEquals(1, readValue.getId());

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("name"));
        assertTrue(deserialized.contains("id"));
    }
}

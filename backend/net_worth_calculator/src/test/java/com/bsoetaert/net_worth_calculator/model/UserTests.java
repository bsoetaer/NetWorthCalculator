package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {
    @Test
    public void serialization() throws Exception {
        String jsonString =
                "{\"userId\":1}";

        ObjectMapper mapper = new ObjectMapper();
        User readValue = mapper.readValue(jsonString, User.class);

        assertEquals(1, readValue.getUserId());

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("userId"));
    }
}

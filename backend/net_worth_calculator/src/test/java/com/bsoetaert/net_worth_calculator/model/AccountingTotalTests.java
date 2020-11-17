package com.bsoetaert.net_worth_calculator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountingTotalTests {
    @Test
    public void serialization() throws Exception {
        String jsonString =
                "{\"assets\":1," +
                        "\"liabilities\":2," +
                        "\"netWorth\":3}";

        ObjectMapper mapper = new ObjectMapper();
        AccountingTotal readValue = mapper.readValue(jsonString, AccountingTotal.class);

        assertEquals(new BigDecimal(String.valueOf(1)), readValue.getAssets());
        assertEquals(new BigDecimal(String.valueOf(2)), readValue.getLiabilities());
        assertEquals(new BigDecimal(String.valueOf(3)), readValue.getNetWorth());

        String deserialized = mapper.writeValueAsString(readValue);
        assertTrue(deserialized.contains("assets"));
        assertTrue(deserialized.contains("liabilities"));
        assertTrue(deserialized.contains("netWorth"));
    }
}

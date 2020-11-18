package com.bsoetaert.net_worth_calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataStoreTests {

    @Test
    public void addUser() {
        DataStore.reset();

        assertFalse(DataStore.hasUserSession(1));
        DataStore.addUser();
        assertTrue(DataStore.hasUserSession(1));
        assertNotNull(DataStore.getSheet(1));
    }

    @Test
    public void getExchangeCreatesExchange() {
        DataStore.reset();

        assertNotNull(DataStore.getExchange());
    }
}

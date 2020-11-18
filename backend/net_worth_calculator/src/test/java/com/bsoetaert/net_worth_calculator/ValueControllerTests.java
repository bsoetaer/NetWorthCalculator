package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.AccountingSheet;
import com.bsoetaert.net_worth_calculator.model.ValueUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ValueControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private final String endpoint = "/value";

    @Test
    public void invalidUserId() throws Exception {
        DataStore.reset();
        this.mockMvc.perform(put(endpoint + "?id=1")).andExpect(status().is4xxClientError());
    }

    @Test
    public void missingId() throws Exception {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        this.mockMvc.perform(put(endpoint + "?userId=" + userId)).andExpect(status().is4xxClientError());
        this.mockMvc.perform(put(endpoint + "?userId=" + userId + "?id=500")).andExpect(status().is4xxClientError());
    }

    @Test
    public void noMatchingAsset() throws Exception {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        CurrencyExchange exchange = createBasicExchange("USD");
        DataStore.setExchange(exchange);

        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setCurrency("CAD");
        valueUpdate.setValue(new BigDecimal(1));
        valueUpdate.setName("value");

        ObjectMapper mapper = new ObjectMapper();
        String serializedUpdate = mapper.writeValueAsString(valueUpdate);

        this.mockMvc.perform(put(endpoint + "?userId=" + userId + "&id=500").content(serializedUpdate).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void queryIdDoesNotMatchBodyId() throws Exception {
        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setCurrency("CAD");
        valueUpdate.setValue(new BigDecimal(1));
        valueUpdate.setName("value");
        valueUpdate.setId(2);

        testClientError(valueUpdate);
    }

    @Test
    public void missingCurrency() throws Exception {
        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setValue(new BigDecimal(1));
        valueUpdate.setName("value");

        testClientError(valueUpdate);
    }

    @Test
    public void invalidCurrency() throws Exception {
        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setCurrency("TOOLONG");
        valueUpdate.setValue(new BigDecimal(1));
        valueUpdate.setName("value");

        testClientError(valueUpdate);
    }

    @Test
    public void unsupportedCurrency() throws Exception {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        CurrencyExchange exchange = createBasicExchange("USD");
        DataStore.setExchange(exchange);

        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setCurrency("AAA");
        valueUpdate.setValue(new BigDecimal(1));
        valueUpdate.setName("value");

        ObjectMapper mapper = new ObjectMapper();
        String serializedUpdate = mapper.writeValueAsString(valueUpdate);

        this.mockMvc.perform(put(endpoint + "?userId=" + userId + "&id=1").content(serializedUpdate).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void missingValue() throws Exception {
        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setCurrency("CAD");
        valueUpdate.setName("value");

        testClientError(valueUpdate);
    }

    @Test
    public void missingName() throws Exception {
        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setCurrency("CAD");
        valueUpdate.setValue(new BigDecimal(1));

        testClientError(valueUpdate);
    }

    @Test
    public void invalidName() throws Exception {
        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setCurrency("CAD");
        valueUpdate.setValue(new BigDecimal(1));
        valueUpdate.setName("nothing");

        testClientError(valueUpdate);
    }

    @Test
    public void success() throws Exception {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        AccountingSheet sheet = DataStore.getSheet(userId);
        CurrencyExchange exchange = createBasicExchange("CAD");
        DataStore.setExchange(exchange);

        Calculator calc = new Calculator(sheet, "CAD", exchange);
        sheet = calc.calculate();

        BigDecimal previousTotal = sheet.getTotals().getNetWorth();
        BigDecimal previousAssetValue = sheet.getItem(1).getValue("value").getValue();
        BigDecimal newAssetValue = new BigDecimal(1);
        BigDecimal expectedTotal = previousTotal.subtract(previousAssetValue).add(newAssetValue).setScale(2, RoundingMode.HALF_UP);
        ObjectMapper mapper = new ObjectMapper();

        ValueUpdate valueUpdate = new ValueUpdate();
        valueUpdate.setCurrency("USD");
        valueUpdate.setValue(new BigDecimal(1));
        valueUpdate.setName("value");

        String serializedUpdate = mapper.writeValueAsString(valueUpdate);

        this.mockMvc.perform(put(endpoint + "?userId=" + userId + "&id=1").content(serializedUpdate).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        assertEquals(expectedTotal, DataStore.getSheet(userId).getTotals().getNetWorth());
    }

    private void testClientError(ValueUpdate valueUpdate) throws Exception
    {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        CurrencyExchange exchange = createBasicExchange("USD");
        DataStore.setExchange(exchange);

        ObjectMapper mapper = new ObjectMapper();
        String serializedUpdate = mapper.writeValueAsString(valueUpdate);

        this.mockMvc.perform(put(endpoint + "?userId=" + userId + "&id=1").content(serializedUpdate).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    private CurrencyExchange createBasicExchange(String targetCurrency) {
        String baseCurrency = "USD";

        BigDecimal exchangeRate1 = new BigDecimal(String.valueOf(1.5));
        CurrencyRate rate1 = new CurrencyRate(targetCurrency);
        rate1.addStartCurrency(baseCurrency, exchangeRate1);

        IRateProvider provider1 = Mockito.mock(IRateProvider.class);
        Mockito.when(provider1.getName()).thenReturn("Mock1");
        Mockito.when(provider1.getRates(anyString())).thenReturn(rate1);
        ArrayList<IRateProvider> providerList1 = new ArrayList<>();
        providerList1.add(provider1);

        return new CurrencyExchange(providerList1);
    }
}

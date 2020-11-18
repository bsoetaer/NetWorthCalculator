package com.bsoetaert.net_worth_calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ConvertControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private final String endpoint = "/convert";

    @Test
    public void invalidUserId() throws Exception {
        DataStore.reset();
        this.mockMvc.perform(get(endpoint + "?currency=CAD")).andExpect(status().is4xxClientError());
    }

    @Test
    public void missingCurrency() throws Exception {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        this.mockMvc.perform(get(endpoint + "?userId=" + userId)).andExpect(status().is4xxClientError());
    }

    @Test
    public void invalidCurrency() throws Exception {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        DataStore.setExchange(createBasicExchange("AAA"));

        this.mockMvc.perform(get(endpoint + "?userId=" + userId + "&currency=AAA"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void success() throws Exception {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        DataStore.setExchange(createBasicExchange("CAD"));
        ObjectMapper mapper = new ObjectMapper();

        String serialized = mapper.writeValueAsString(DataStore.getSheet(userId));

        this.mockMvc.perform(get(endpoint + "?userId=" + userId + "&currency=CAD"))
                .andExpect(status().isOk())
                .andExpect(content().json(serialized));
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

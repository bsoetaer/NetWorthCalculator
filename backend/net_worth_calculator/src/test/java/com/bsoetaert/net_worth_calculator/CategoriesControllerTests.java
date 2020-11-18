package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.AccountingCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class CategoriesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final String endpoint = "/categories";

    @Test
    public void invalidUserId() throws Exception {
        DataStore.reset();
        this.mockMvc.perform(get(endpoint)).andExpect(status().is4xxClientError());
    }

    @Test
    public void success() throws Exception {
        DataStore.reset();
        Integer userId = DataStore.addUser();
        List<AccountingCategory> expectedCategories = DataStore.getSheet(userId).getCategories();
        ObjectMapper mapper = new ObjectMapper();

        String serialized = mapper.writeValueAsString(expectedCategories);

        this.mockMvc.perform(get(endpoint + "?userId=" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(serialized));
    }
}

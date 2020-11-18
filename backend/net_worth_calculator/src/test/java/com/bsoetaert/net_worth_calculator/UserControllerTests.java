package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private final String endpoint = "/user";

    @Test
    public void success() throws Exception {
        DataStore.reset();
        User user = new User();
        user.setUserId(1);

        assertFalse(DataStore.hasUserSession(1));

        ObjectMapper mapper = new ObjectMapper();
        String serialized = mapper.writeValueAsString(user);

        this.mockMvc.perform(post(endpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(serialized));

        assertTrue(DataStore.hasUserSession(1));
    }
}

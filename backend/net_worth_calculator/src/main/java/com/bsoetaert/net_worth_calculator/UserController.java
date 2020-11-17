package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser() {
        Integer userId = DataStore.addUser();
        User user = new User();
        user.setUserId(userId);
        return user;
    }
}

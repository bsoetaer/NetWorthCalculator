package com.bsoetaert.net_worth_calculator;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class LocalDateTimeDisplay {

    @GetMapping(value = "/time", produces = MediaType.APPLICATION_JSON_VALUE)
    public String displayCurrentTime() {
        return LocalDateTime.now().toString();
    }
}

package com.bsoetaert.net_worth_calculator;

import com.bsoetaert.net_worth_calculator.model.AccountingCategory;
import com.bsoetaert.net_worth_calculator.model.AccountingItem;
import com.bsoetaert.net_worth_calculator.model.AccountingSheet;
import com.bsoetaert.net_worth_calculator.model.AccountingValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataGenerator {
    public static AccountingSheet createBaseData() {
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File(DataGenerator.class.getResource("/static/BaseData.json").getPath());

        AccountingSheet sheet = null;

        try {
            sheet = objectMapper.readValue(file, AccountingSheet.class);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read base data.", e);
        }

        return sheet;
    }
}

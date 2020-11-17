package com.bsoetaert.net_worth_calculator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class ExchangeRatesApiProvider implements IRateProvider  {

    Logger logger = LoggerFactory.getLogger(ExchangeRatesApiProvider.class);
    private String name = "ExchangeRatesApi";
    private String baseUrl = "https://api.exchangeratesapi.io/latest";

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CurrencyRate getRates(String targetCurrency) {
        JSONObject ratesObject = null;
        try {
            JSONObject responseObject = requestRates(targetCurrency);
            ratesObject = extractRates(responseObject);
        }
        catch(Exception e) {
            return null;
        }

        return convertToCurrencyRate(targetCurrency, ratesObject);
    }

    private JSONObject requestRates(String baseCurrency) throws Exception {
        URL url = new URL(baseUrl + "?base=" + baseCurrency);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if(responseCode != HttpStatus.OK.value())
        {
            String message = "Failed to get rates for " + baseCurrency + ". " +
                    "Status Code: " + responseCode + ", Message: " + response;
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
        return (JSONObject) (new JSONParser()).parse(response.toString());
    }

    private JSONObject extractRates(JSONObject responseObject)  {

        if(!responseObject.containsKey("rates")) {
            String message = "Unable to find rates element.";
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }

        return (JSONObject) responseObject.get("rates");
    }

    private CurrencyRate convertToCurrencyRate(String targetCurrency, JSONObject ratesObject) {
        CurrencyRate rate = new CurrencyRate(targetCurrency);

        for(Iterator iterator = ratesObject.keySet().iterator(); iterator.hasNext();) {
            String originalCurrency = (String) iterator.next();
            BigDecimal exchangeRate = null;
            if(ratesObject.get(originalCurrency) instanceof Long ) {
                exchangeRate = new BigDecimal((Long) ratesObject.get(originalCurrency));
            }
            else if(ratesObject.get(originalCurrency) instanceof Double ){
                exchangeRate = new BigDecimal((Double) ratesObject.get(originalCurrency));
            }
            else {
                continue;
            }

            BigDecimal reverseRate = (new BigDecimal(1).divide(exchangeRate,10, RoundingMode.HALF_UP));
            rate.addStartCurrency(originalCurrency, reverseRate);
        }

        // 2 because rate always has 1 entry for itself. If we did not add any valid rates, return null
        if(rate.getConversionRates().size() < 2) {
            return null;
        }

        return rate;
    }
}

package com.bsoetaert.net_worth_calculator;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeRatesApiProviderTests {

    private ClientAndServer mockServer;

    private final String baseCurrency = "CAD";
    private final String targetCurrency = "USD";

    @BeforeEach
    public void startServer() {
        mockServer = startClientAndServer(1080);
    }

    @AfterEach
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    public void getRatesSuccess() {
        JSONObject jsonResponse = new JSONObject();
        JSONObject rates = new JSONObject();
        double conversionRate = 1.3;
        rates.put(targetCurrency, conversionRate);
        jsonResponse.put("rates", rates);

        mockServer.when(HttpRequest.request().withPath("/latest")
            .withQueryStringParameter("base", "CAD")
        )
        .respond(
            HttpResponse.response()
                .withStatusCode(200)
                .withBody(jsonResponse.toJSONString())
        );

        ExchangeRatesApiProvider provider = new ExchangeRatesApiProvider();

        provider.setBaseUrl(getMockUrl());

        CurrencyRate rate = provider.getRates(baseCurrency);
        assertNotNull(rate);
        assertTrue(rate.canConvert(baseCurrency));
        assertTrue(rate.canConvert(targetCurrency));
        assertEquals(baseCurrency, rate.getTargetCurrency());

        BigDecimal expectedConversionRate = (new BigDecimal(1).divide(new BigDecimal(conversionRate),10, RoundingMode.HALF_UP));
        assertEquals(expectedConversionRate, rate.convertFrom(new BigDecimal(1), targetCurrency));
        assertEquals(new BigDecimal(1), rate.convertFrom(new BigDecimal(1), baseCurrency));
    }

    @Test
    public void missingRatesElement() {
        JSONObject jsonResponse = new JSONObject();

        mockServer.when(HttpRequest.request().withPath("/latest")
                .withQueryStringParameter("base", "CAD")
        )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(jsonResponse.toJSONString())
                );

        ExchangeRatesApiProvider provider = new ExchangeRatesApiProvider();

        provider.setBaseUrl(getMockUrl());

        CurrencyRate rate = provider.getRates(baseCurrency);
        assertNull(rate);
    }

    @Test
    public void emptyRates() {
        JSONObject jsonResponse = new JSONObject();
        JSONObject rates = new JSONObject();
        jsonResponse.put("rates", rates);

        mockServer.when(HttpRequest.request().withPath("/latest")
                .withQueryStringParameter("base", "CAD")
        )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(jsonResponse.toJSONString())
                );

        ExchangeRatesApiProvider provider = new ExchangeRatesApiProvider();

        provider.setBaseUrl(getMockUrl());

        CurrencyRate rate = provider.getRates(baseCurrency);
        assertNull(rate);
    }

    @Test
    public void ratesWrongType() {
        JSONObject jsonResponse = new JSONObject();
        JSONObject rates = new JSONObject();
        rates.put(targetCurrency, "ABC");
        rates.put("AUD", 5);
        jsonResponse.put("rates", rates);

        mockServer.when(HttpRequest.request().withPath("/latest")
                .withQueryStringParameter("base", "CAD")
        )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(jsonResponse.toJSONString())
                );

        ExchangeRatesApiProvider provider = new ExchangeRatesApiProvider();

        provider.setBaseUrl(getMockUrl());

        CurrencyRate rate = provider.getRates(baseCurrency);
        assertNotNull(rate);
        assertTrue(rate.canConvert(baseCurrency));
        assertTrue(rate.canConvert("AUD"));
        assertFalse(rate.canConvert(targetCurrency));
    }

    private String getMockUrl() {
        return "http://" +mockServer.remoteAddress().getHostName() + ":" + mockServer.remoteAddress().getPort() + "/latest";
    }
}

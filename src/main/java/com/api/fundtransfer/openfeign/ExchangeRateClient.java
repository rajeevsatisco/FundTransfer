package com.api.fundtransfer.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Feign client interface for retrieving exchange rates from an external API.
 */
@FeignClient(name = "exchangeRateClient", url = "${exchange.rate.api.url}")
public interface ExchangeRateClient {

    /**
     * GET method to fetch exchange rates based on API key, from currency, and to currency.
     *
     * @param api_key      the API key required for authentication
     * @param fromCurrency the currency from which the exchange rate is calculated
     * @param toCurrency   the target currency to which the exchange rate is calculated
     * @return a map containing exchange rate information
     */
    @GetMapping
    Map<String, Object> getExchangeRates(@RequestParam("api_key") String api_key,
                                         @RequestParam("from") String fromCurrency,
                                         @RequestParam("to") String toCurrency
    );
}
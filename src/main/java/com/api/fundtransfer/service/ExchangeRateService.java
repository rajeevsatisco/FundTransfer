package com.api.fundtransfer.service;

import com.api.fundtransfer.exception.ExchangeRateRetrievalException;
import com.api.fundtransfer.openfeign.ExchangeRateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static com.api.fundtransfer.constant.Constants.EXCHANGE_RATE_RETRIEVAL_EXCEPTION;

@Service
@Validated
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    private final ExchangeRateClient exchangeRateClient;

    @Value("${exchange.rate.api.key}")
    private String apiKey;

    @Autowired
    public ExchangeRateService(ExchangeRateClient exchangeRateClient) {
        this.exchangeRateClient = exchangeRateClient;
    }

    /**
     * Retrieves the exchange rate from one currency to another.
     *
     * @param fromCurrency the source currency
     * @param toCurrency   the target currency
     * @return the exchange rate as BigDecimal
     * @throws ExchangeRateRetrievalException if the exchange rate cannot be retrieved
     */
    @Transactional(readOnly = true)
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) throws ExchangeRateRetrievalException {
        logger.debug("Retrieving exchange rate from {} to {}", fromCurrency, toCurrency);
        try {
            Map<String, Object> response = exchangeRateClient.getExchangeRates(apiKey, fromCurrency, toCurrency);
            Map<String, Double> rates = (Map<String, Double>) response.get("results");
            Optional<Double> exchangeRateOptional = Optional.ofNullable(rates)
                    .map(r -> r.get(toCurrency));

            Double exchangeRate = exchangeRateOptional.orElseThrow(() -> {
                logger.error("Exchange rate not found for toCurrency: {}", toCurrency);
                return new ExchangeRateRetrievalException(EXCHANGE_RATE_RETRIEVAL_EXCEPTION);
            });

            return BigDecimal.valueOf(exchangeRate);
        } catch (Exception e) {
            logger.error("Error retrieving exchange rate from {} to {}", fromCurrency, toCurrency, e);
            throw new ExchangeRateRetrievalException(EXCHANGE_RATE_RETRIEVAL_EXCEPTION);
        }
    }
}
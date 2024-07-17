package com.api.fundtransfer;

import com.api.fundtransfer.exception.ExchangeRateRetrievalException;
import com.api.fundtransfer.openfeign.ExchangeRateClient;
import com.api.fundtransfer.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.api.fundtransfer.constant.Constants.EXCHANGE_RATE_RETRIEVAL_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateClient exchangeRateClient;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(exchangeRateService, "apiKey", "test-api-key");
    }

    @Test
    public void testGetExchangeRate_Success() throws ExchangeRateRetrievalException {
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        Map<String, Object> response = new HashMap<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put(toCurrency, 0.85);
        response.put("results", rates);

        when(exchangeRateClient.getExchangeRates("test-api-key", fromCurrency, toCurrency)).thenReturn(response);

        BigDecimal exchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);

        assertEquals(BigDecimal.valueOf(0.85), exchangeRate);
        verify(exchangeRateClient, times(1)).getExchangeRates("test-api-key", fromCurrency, toCurrency);
    }

    @Test
    public void testGetExchangeRate_Exception() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";

        when(exchangeRateClient.getExchangeRates("test-api-key", fromCurrency, toCurrency)).thenThrow(new RuntimeException("API error"));

        ExchangeRateRetrievalException exception = assertThrows(ExchangeRateRetrievalException.class, () -> {
            exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        });

        assertEquals(EXCHANGE_RATE_RETRIEVAL_EXCEPTION, exception.getMessage());
        verify(exchangeRateClient, times(1)).getExchangeRates("test-api-key", fromCurrency, toCurrency);
    }
@Test
    public void testGetExchangeRate_Exception1() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";

        when(exchangeRateClient.getExchangeRates("test-api-key", fromCurrency, toCurrency)).thenThrow(new ExchangeRateRetrievalException("Exchange rate not retrieved"));

        ExchangeRateRetrievalException exception = assertThrows(ExchangeRateRetrievalException.class, () -> {
            exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        });

        assertEquals(EXCHANGE_RATE_RETRIEVAL_EXCEPTION, exception.getMessage());
        verify(exchangeRateClient, times(1)).getExchangeRates("test-api-key", fromCurrency, toCurrency);
    }
}

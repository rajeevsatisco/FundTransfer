package com.api.fundtransfer;

import com.api.fundtransfer.controller.FundTransferController;
import com.api.fundtransfer.dto.FundTransferRequest;
import com.api.fundtransfer.dto.FundTransferResponse;
import com.api.fundtransfer.exception.AccountNotExistException;
import com.api.fundtransfer.exception.BalanceNotSufficientException;
import com.api.fundtransfer.service.FundTransferService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FundTransferControllerTest {

    @InjectMocks
    private FundTransferController fundTransferController;

    @Mock
    private FundTransferService fundTransferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void testTransferFunds_Success() throws AccountNotExistException, BalanceNotSufficientException {
        FundTransferRequest request = new FundTransferRequest();
        doNothing().when(fundTransferService).transferFunds(any(FundTransferRequest.class));

        ResponseEntity response = fundTransferController.transferFunds(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(fundTransferService, times(1)).transferFunds(request);
    }

    @Test
    void testTransferFunds_AccountNotExistException() throws AccountNotExistException, BalanceNotSufficientException {
        FundTransferRequest request = new FundTransferRequest();
        doThrow(new AccountNotExistException("Account does not exist")).when(fundTransferService).transferFunds(any(FundTransferRequest.class));

        try {
            fundTransferController.transferFunds(request);
        } catch (AccountNotExistException e) {
            assertEquals("Account does not exist", e.getMessage());
        }
    }

    @Test
    void testTransferFunds_BalanceNotSufficientException() throws AccountNotExistException, BalanceNotSufficientException {
        FundTransferRequest request = new FundTransferRequest();
        doThrow(new BalanceNotSufficientException("Balance not sufficient")).when(fundTransferService).transferFunds(any(FundTransferRequest.class));

        try {
            fundTransferController.transferFunds(request);
        } catch (BalanceNotSufficientException e) {
            assertEquals("Balance not sufficient", e.getMessage());
        }
    }

    @Test
    void testGetAllAccount() {
        List<FundTransferResponse> transactions = new ArrayList<>();
        when(fundTransferService.getTransactions()).thenReturn(transactions);
        ResponseEntity<List<FundTransferResponse>> response = fundTransferController.getTransactions();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(fundTransferService, times(1)).getTransactions();
    }
}

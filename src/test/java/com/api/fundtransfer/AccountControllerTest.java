package com.api.fundtransfer;

import com.api.fundtransfer.controller.AccountController;
import com.api.fundtransfer.dto.AccountRequest;
import com.api.fundtransfer.dto.AccountResponse;
import com.api.fundtransfer.exception.AccountNotExistException;
import com.api.fundtransfer.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);
    }

    @Test
    public void testCreateAccount() {
        AccountRequest accountRequest = new AccountRequest();
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(1L);

        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountController.createAccount(accountRequest);
        URI expectedUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{accountId}").buildAndExpand(accountResponse.getAccountId()).toUri();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedUri, response.getHeaders().getLocation());
        assertEquals(accountResponse, response.getBody());
    }

    @Test
    public void testGetAllAccount() {
        AccountResponse accountResponse1 = new AccountResponse();
        AccountResponse accountResponse2 = new AccountResponse();
        List<AccountResponse> accountResponses = Arrays.asList(accountResponse1, accountResponse2);

        when(accountService.getAllAccount()).thenReturn(accountResponses);

        ResponseEntity<List<AccountResponse>> response = accountController.getAllAccount();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountResponses, response.getBody());
    }

    @Test
    public void testGetAccount() throws AccountNotExistException {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(1L);

        when(accountService.getAccountById(1L)).thenReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountController.getAccount(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountResponse, response.getBody());
    }

    @Test
    public void testDeactivateAccount() throws AccountNotExistException {
        doNothing().when(accountService).deactivateAccountById(1L);

        ResponseEntity<HttpStatus> response = accountController.deactivateAccount(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(accountService, times(1)).deactivateAccountById(1L);
    }

    @Test
    public void testActivateAccount() throws AccountNotExistException {
        doNothing().when(accountService).activateAccountById(1L);

        ResponseEntity<HttpStatus> response = accountController.activateAccount(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(accountService, times(1)).activateAccountById(1L);
    }

    @Test
    public void testUpdateAccount() {
        AccountRequest accountRequest = new AccountRequest();
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(1L);

        when(accountService.updateAccount(any(AccountRequest.class), eq(1L))).thenReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountController.updateAccount(accountRequest, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountResponse, response.getBody());
    }
}

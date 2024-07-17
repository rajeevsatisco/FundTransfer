package com.api.fundtransfer;

import com.api.fundtransfer.dto.AccountRequest;
import com.api.fundtransfer.dto.AccountResponse;
import com.api.fundtransfer.entity.Account;
import com.api.fundtransfer.exception.AccountNotExistException;
import com.api.fundtransfer.exception.ErrorResponse;
import com.api.fundtransfer.repository.AccountRepository;
import com.api.fundtransfer.service.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setAccountId(1L);
        account.setOwnerId(1L);
        account.setBalance(BigDecimal.valueOf(1000.0));
    }

    @Test
    void testGetAccountById() {
        when(accountRepository.findByAccountIdAndStatus(1L, "ACTIVE")).thenReturn(Optional.of(account));
        AccountResponse response = accountService.getAccountById(1L);
        assertNotNull(response);
        assertEquals(account.getAccountId(), response.getAccountId());
    }

    @Test
    void testGetAccountById_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        AccountNotExistException exception = assertThrows(AccountNotExistException.class, () -> accountService.getAccountById(1L));

        assertEquals("Either the debit or the credit account does not exist", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        ErrorResponse.ErrorResponseBuilder builder = mock(ErrorResponse.ErrorResponseBuilder.class);
        when(builder.timestamp(any(LocalDateTime.class))).thenReturn(builder);
        when(builder.message(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(errorResponse);

        Assertions.assertEquals("Either the debit or the credit account does not exist", errorResponse.getMessage());
        assertEquals(LocalDateTime.now().getDayOfMonth(), errorResponse.getTimestamp().getDayOfMonth());
    }

    @Test
    void testCreateAccount() {
        AccountRequest request = new AccountRequest();
        request.setOwnerId(1L);
        request.setBalance(BigDecimal.valueOf(1000.0));

        when(accountRepository.save(any(Account.class))).thenReturn(account);
        AccountResponse response = accountService.createAccount(request);
        assertNotNull(response);
        assertEquals(account.getAccountId(), response.getAccountId());
        assertEquals(request.getOwnerId(), response.getOwnerId());
        assertEquals(request.getBalance(), response.getBalance());
    }

    @Test
    void testGetAllAccount() {
        when(accountRepository.findAll()).thenReturn(Arrays.asList(account, new Account()));
        List<AccountResponse> responses = accountService.getAllAccount();
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    void testDeactivateAccountById() {
        when(accountRepository.findByAccountIdAndStatus(1L,"INACTIVE")).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).deleteById(1L);
        accountService.deactivateAccountById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testDeactivateAccountById_AccountNotFound() {
        when(accountRepository.findByAccountIdAndStatus(2L, "INACTIVE")).thenReturn(Optional.empty());

        accountService.deactivateAccountById(2L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testUpdateAccount() {
        AccountRequest request = new AccountRequest();
        request.setOwnerId(1L);
        request.setBalance(BigDecimal.valueOf(1000.0));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse response = accountService.updateAccount(request, 1L);
        assertNotNull(response);
        assertEquals(account.getAccountId(), response.getAccountId());
        assertEquals(request.getOwnerId(), response.getOwnerId());
        assertEquals(request.getBalance(), response.getBalance());
    }

    @Test
    void testUpdateAccount_AccountNotFound() {
        AccountRequest request = new AccountRequest();
        request.setOwnerId(1L);
        request.setBalance(BigDecimal.valueOf(1500.0));

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotExistException.class, () -> accountService.updateAccount(request, 1L));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testActivateAccountById() {
        when(accountRepository.findByAccountIdAndStatus(1L,"ACTIVE")).thenReturn(Optional.of(account));
        accountService.activateAccountById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testActivateAccountById_AccountNotFound() {
        when(accountRepository.findByAccountIdAndStatus(2L, "ACTIVE")).thenReturn(Optional.empty());

        accountService.activateAccountById(2L);
        verify(accountRepository, never()).save(any(Account.class));
    }
}

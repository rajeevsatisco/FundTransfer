package com.api.fundtransfer;

import com.api.fundtransfer.dto.FundTransferRequest;
import com.api.fundtransfer.dto.FundTransferResponse;
import com.api.fundtransfer.entity.Account;
import com.api.fundtransfer.entity.FundTransfer;
import com.api.fundtransfer.exception.AccountNotExistException;
import com.api.fundtransfer.exception.SimilarAccountException;
import com.api.fundtransfer.mapper.FundTransferMapper;
import com.api.fundtransfer.repository.AccountRepository;
import com.api.fundtransfer.repository.FundTransferRepository;
import com.api.fundtransfer.service.ExchangeRateService;
import com.api.fundtransfer.service.FundTransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundTransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private FundTransferRepository fundTransferRepository;

    @InjectMocks
    private FundTransferServiceImpl fundTransferService;

    private FundTransferRequest validRequest;
    private Account mockFromAccount;
    private Account mockToAccount;
    private FundTransfer fundTransfer1;
    private FundTransfer fundTransfer2;
    private FundTransferResponse fundTransferResponse1;
    private FundTransferResponse fundTransferResponse2;

    @BeforeEach
    void setUp() {
        validRequest = new FundTransferRequest(1L, 2L, new BigDecimal("100.00"));

        mockFromAccount = new Account();
        mockFromAccount.setAccountId(1L);
        mockFromAccount.setBalance(new BigDecimal("500.00"));
        mockFromAccount.setCurrency("EUR");
        mockToAccount = new Account();
        mockToAccount.setAccountId(2L);
        mockToAccount.setCurrency("USD");
        mockToAccount.setBalance(new BigDecimal("1000.00"));
        // Initialize mock entities and DTOs
        fundTransfer1 = new FundTransfer();
        fundTransfer1.setTransferId(1L);
        fundTransfer1.setAccountFromId(1001L);
        fundTransfer1.setAccountToId(1002L);
        fundTransfer1.setAmount(BigDecimal.valueOf(1000.00));

        fundTransfer2 = new FundTransfer();
        fundTransfer2.setTransferId(2L);
        fundTransfer2.setAccountFromId(1003L);
        fundTransfer2.setAccountToId(1004L);
        fundTransfer2.setAmount(BigDecimal.valueOf(2000.00));

        fundTransferResponse1 = FundTransferMapper.INSTANCE.entityToDTO(fundTransfer1);
        fundTransferResponse2 = FundTransferMapper.INSTANCE.entityToDTO(fundTransfer2);
    }

    @Test
    void transferFunds_ValidTransfer_ShouldTransferSuccessfully() {

        when(accountRepository.findByAccountIdAndStatus(1L, "ACTIVE")).thenReturn(Optional.of(mockFromAccount));
        when(accountRepository.findByAccountIdAndStatus(2L, "ACTIVE")).thenReturn(Optional.of(mockToAccount));
        when(exchangeRateService.getExchangeRate(any(), any())).thenReturn(BigDecimal.ONE);

        fundTransferService.transferFunds(validRequest);

        verify(accountRepository, times(2)).save(any(Account.class));
        verify(fundTransferRepository, times(1)).save(any());
    }

    @Test
    void transferFunds_SimilarAccounts_ShouldThrowSimilarAccountException() {
        FundTransferRequest requestWithSameAccounts = new FundTransferRequest(1L, 1L, new BigDecimal("100.00"));

        assertThrows(SimilarAccountException.class, () -> fundTransferService.transferFunds(requestWithSameAccounts));
        verify(accountRepository, never()).findById(any());
        verify(accountRepository, never()).save(any());
        verify(fundTransferRepository, never()).save(any());
    }

    @Test
    void transferFunds_AccountFromNotFound_ShouldThrowAccountNotExistException() {
        when(accountRepository.findByAccountIdAndStatus(any(), anyString())).thenReturn(Optional.empty());

        assertThrows(AccountNotExistException.class, () -> fundTransferService.transferFunds(validRequest));
        verify(accountRepository, times(1)).findByAccountIdAndStatus(1L, "ACTIVE");
        verify(accountRepository, never()).findByAccountIdAndStatus(2L, "ACTIVE");
        verify(accountRepository, never()).save(any());
        verify(fundTransferRepository, never()).save(any());
    }

    @Test
    void transferFunds_BalanceNotSufficient_ShouldThrowBalanceNotSufficientException() {
        mockFromAccount.setBalance(new BigDecimal("50.00"));
        when(accountRepository.findByAccountIdAndStatus(1L, "ACTIVE")).thenReturn(Optional.of(mockFromAccount));
        assertThrows(AccountNotExistException.class, () -> fundTransferService.transferFunds(validRequest));
        verify(accountRepository, times(1)).findByAccountIdAndStatus(1L, "ACTIVE");
        verify(accountRepository, never()).save(any());
        verify(fundTransferRepository, never()).save(any());
    }

    @Test
    public void testGetTransactions() {
        // Mock the repository response
        when(fundTransferRepository.findAll()).thenReturn(Arrays.asList(fundTransfer1, fundTransfer2));

        // Call the service method
        List<FundTransferResponse> result = fundTransferService.getTransactions();

        // Verify the results
        assertEquals(2, result.size());
        assertEquals(fundTransferResponse1, result.get(0));
        assertEquals(fundTransferResponse2, result.get(1));

        // Verify repository interaction
        verify(fundTransferRepository, times(1)).findAll();
    }
}

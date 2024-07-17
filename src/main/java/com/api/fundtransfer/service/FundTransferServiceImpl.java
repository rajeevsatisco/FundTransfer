package com.api.fundtransfer.service;

import com.api.fundtransfer.dto.FundTransferRequest;
import com.api.fundtransfer.dto.FundTransferResponse;
import com.api.fundtransfer.entity.Account;
import com.api.fundtransfer.exception.AccountNotExistException;
import com.api.fundtransfer.exception.BalanceNotSufficientException;
import com.api.fundtransfer.exception.SimilarAccountException;
import com.api.fundtransfer.mapper.FundTransferMapper;
import com.api.fundtransfer.repository.AccountRepository;
import com.api.fundtransfer.repository.FundTransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.fundtransfer.constant.Constants.*;

/**
 * Implementation of FundTransferService interface for handling fund transfer operations.
 */
@Service
public class FundTransferServiceImpl implements FundTransferService {
    private static final Logger logger = LoggerFactory.getLogger(FundTransferServiceImpl.class);

    private final AccountRepository accountRepository;
    private final ExchangeRateService exchangeRateService;
    private final FundTransferRepository fundTransferRepository;

    @Autowired
    public FundTransferServiceImpl(AccountRepository accountRepository, ExchangeRateService exchangeRateService, FundTransferRepository fundTransferRepository) {
        this.accountRepository = accountRepository;
        this.exchangeRateService = exchangeRateService;
        this.fundTransferRepository = fundTransferRepository;
    }

    /**
     * Transfers funds based on the given fund transfer request.
     *
     * @param fundTransferRequest the fund transfer request DTO containing details of the transaction
     * @throws AccountNotExistException      if either the debit or credit account does not exist
     * @throws BalanceNotSufficientException if the balance of the debit account is not sufficient
     */
    @Override
    //@Transactional
    public synchronized void transferFunds(FundTransferRequest fundTransferRequest) {
        if (fundTransferRequest.getAccountFromId() == fundTransferRequest.getAccountToId()) {
            throw new SimilarAccountException(SIMILAR_ACCOUNT_EXCEPTION);
        }
        // Retrieve the debit account from the database or throw exception if not found
        Account fromAccount = accountRepository.findByAccountIdAndStatus(fundTransferRequest.getAccountFromId(), "ACTIVE")
                .orElseThrow(() -> new AccountNotExistException(ACCOUNT_NOT_EXIST_EXCEPTION));
        // Retrieve the credit account from the database or throw exception if not found
        Account toAccount = accountRepository.findByAccountIdAndStatus(fundTransferRequest.getAccountToId(), "ACTIVE")
                .orElseThrow(() -> new AccountNotExistException(ACCOUNT_NOT_EXIST_EXCEPTION));

        // Check if the balance of the debit account is sufficient for the transfer amount
        if (fromAccount.getBalance().compareTo(fundTransferRequest.getAmount()) < 0) {
            throw new BalanceNotSufficientException("The balance of the debit account " + fromAccount.getAccountId() + " is not sufficient.");
        }

        BigDecimal exchangeRate = getExchangeRate(fromAccount, toAccount);
        synchronized (this) {
            // Calculate the equivalent amount in the credit account's currency
            BigDecimal convertedAmount = fundTransferRequest.getAmount().multiply(exchangeRate);
            // Perform the funds transfer between the accounts
            fromAccount.setBalance(fromAccount.getBalance().subtract(fundTransferRequest.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(convertedAmount));
            // Save the updated balances of both accounts and record the fund transfer transaction
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        }

        fundTransferRepository.save(FundTransferMapper.INSTANCE.dTOToEntity(fundTransferRequest));

        // Log the successful completion of the fund transfer
        logger.info(TRANSFER_SUCCESS);
    }

    private BigDecimal getExchangeRate(Account fromAccount, Account toAccount) {
        BigDecimal exchangeRate;
        // Get the exchange rate between the currencies of the debit and credit accounts
        if (fromAccount.getCurrency().equalsIgnoreCase(toAccount.getCurrency())) {
            exchangeRate = BigDecimal.valueOf(1);
        } else {
            exchangeRate = exchangeRateService.getExchangeRate(fromAccount.getCurrency(), toAccount.getCurrency());
        }
        return exchangeRate;
    }

    /**
     * Retrieves all fund transfer transactions.
     *
     * @return a list of fund transfer response DTOs representing all transactions
     */
    @Override
    public List<FundTransferResponse> getTransactions() {
        // Retrieve all fund transfer entities from the repository and map them to response DTOs
        return fundTransferRepository.findAll().stream()
                .map(FundTransferMapper.INSTANCE::entityToDTO)
                .collect(Collectors.toList());
    }
}
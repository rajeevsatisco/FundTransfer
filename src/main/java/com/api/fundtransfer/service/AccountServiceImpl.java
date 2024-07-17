package com.api.fundtransfer.service;

import com.api.fundtransfer.constant.AccountStatus;
import com.api.fundtransfer.dto.AccountRequest;
import com.api.fundtransfer.dto.AccountResponse;
import com.api.fundtransfer.entity.Account;
import com.api.fundtransfer.exception.AccountNotExistException;
import com.api.fundtransfer.mapper.AccountMapper;
import com.api.fundtransfer.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.fundtransfer.constant.Constants.ACCOUNT_NOT_EXIST_EXCEPTION;

@Service
@Validated
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return the account response DTO
     */
    @Override
    public AccountResponse getAccountById(Long accountId) {
        logger.debug("Getting account by ID: {}", accountId);
        return accountRepository.findByAccountIdAndStatus(accountId, String.valueOf(AccountStatus.ACTIVE))
                .map(AccountMapper.INSTANCE::entityToDTO)
                .orElseThrow(() -> {
                    logger.error("Account with ID {} not found", accountId);
                    return new AccountNotExistException(ACCOUNT_NOT_EXIST_EXCEPTION);
                });
    }

    /**
     * Creates a new account.
     *
     * @param accountRequest the account request DTO
     * @return the created account response DTO
     */
    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {
        logger.debug("Creating account with request: {}", accountRequest);
        Account account = AccountMapper.INSTANCE.dTOToEntity(accountRequest);
        account.setStatus(String.valueOf(AccountStatus.ACTIVE));
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.INSTANCE.entityToDTO(savedAccount);
    }

    /**
     * Retrieves all accounts.
     *
     * @return the list of account response DTOs
     */
    @Override
    public List<AccountResponse> getAllAccount() {
        logger.debug("Getting all accounts");
        return accountRepository.findAll().stream()
                .map(AccountMapper.INSTANCE::entityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deactivating an account by its ID.
     *
     * @param accountId the ID of the account to delete
     */
    @Override
    @Transactional
    public void deactivateAccountById(Long accountId) {
        logger.debug("Deactivating account by ID: {}", accountId);
        accountRepository.findById(accountId)
                .ifPresentOrElse(existingAccount -> {
                            existingAccount.setStatus(String.valueOf(AccountStatus.INACTIVE));
                            accountRepository.save(existingAccount);
                        }, () ->
                                new AccountNotExistException(ACCOUNT_NOT_EXIST_EXCEPTION)
                );
    }

    /**
     * Updates an existing account.
     *
     * @param accountRequest the account request DTO
     * @param accountId      the ID of the account to update
     * @return the updated account response DTO
     */
    @Override
    @Transactional
    public AccountResponse updateAccount(AccountRequest accountRequest, Long accountId) {
        logger.debug("Updating account with ID: {} using request: {}", accountId, accountRequest);
        return accountRepository.findById(accountId)
                .map(existingAccount -> {
                    Account updatedAccount = AccountMapper.INSTANCE.dTOToEntity(accountRequest);
                    updatedAccount.setAccountId(existingAccount.getAccountId());
                    updatedAccount.setStatus(String.valueOf(AccountStatus.ACTIVE));
                    updatedAccount.setLastUpdatedDate(LocalDateTime.now());
                    Account savedAccount = accountRepository.save(updatedAccount);
                    return AccountMapper.INSTANCE.entityToDTO(savedAccount);
                })
                .orElseThrow(() -> {
                    logger.error("Account with ID {} not found", accountId);
                    return new AccountNotExistException(ACCOUNT_NOT_EXIST_EXCEPTION);
                });
    }

    /**
     * Activating an account by its ID.
     *
     * @param accountId the ID of the account to delete
     */
    @Override
    @Transactional
    public void activateAccountById(Long accountId) {
        logger.debug("Activating account by ID: {}", accountId);
        accountRepository.findById(accountId)
                .ifPresentOrElse(existingAccount -> {
                            existingAccount.setStatus(String.valueOf(AccountStatus.ACTIVE));
                            accountRepository.save(existingAccount);
                        }, () ->
                                new AccountNotExistException(ACCOUNT_NOT_EXIST_EXCEPTION)
                );
    }
}

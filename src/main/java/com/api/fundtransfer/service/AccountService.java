package com.api.fundtransfer.service;

import com.api.fundtransfer.dto.AccountRequest;
import com.api.fundtransfer.dto.AccountResponse;

import java.util.List;

public interface AccountService {

    /**
     * Retrieves an account by its ID.
     *
     * @param accountId the ID of the account
     * @return the account response DTO
     */
    AccountResponse getAccountById(Long accountId);

    /**
     * Creates a new account.
     *
     * @param accountRequest the account request DTO
     * @return the created account response DTO
     */
    AccountResponse createAccount(AccountRequest accountRequest);

    /**
     * Retrieves all accounts.
     *
     * @return a list of account response DTOs
     */
    List<AccountResponse> getAllAccount();

    /**
     * Deactivate an account by its ID.
     *
     * @param accountId the ID of the account to be deleted
     */
    void deactivateAccountById(Long accountId);

    /**
     * Updates an existing account.
     *
     * @param accountRequest the account request DTO
     * @param accountId      the ID of the account to be updated
     * @return the updated account response DTO
     */
    AccountResponse updateAccount(AccountRequest accountRequest, Long accountId);
    /**
     * Activate an account by its ID.
     *
     * @param accountId the ID of the account to be deleted
     */
    void activateAccountById(Long accountId);
}

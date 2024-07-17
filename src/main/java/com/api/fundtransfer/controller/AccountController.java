package com.api.fundtransfer.controller;

import com.api.fundtransfer.configuration.Views;
import com.api.fundtransfer.dto.AccountRequest;
import com.api.fundtransfer.dto.AccountResponse;
import com.api.fundtransfer.exception.AccountNotExistException;
import com.api.fundtransfer.service.AccountService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller class for handling Account-related API operations.
 */
@Tag(name = "Account")
@RestController
@Validated
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Endpoint to create a new account.
     *
     * @param accountRequest the request body containing account details
     * @return ResponseEntity containing the created account response and HTTP status 201
     */
    @Operation(summary = "Create a new account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully", content = @Content(schema = @Schema(implementation = AccountResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PostMapping
    @JsonView(Views.Create.class)
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        AccountResponse createdAccount = accountService.createAccount(accountRequest);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(createdAccount.getAccountId())
                .toUri();
        return ResponseEntity.created(uri).body(createdAccount);
    }

    /**
     * Endpoint to retrieve all accounts.
     *
     * @return ResponseEntity containing a list of account responses and HTTP status 200
     */
    @Operation(summary = "Get all account details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully", content = @Content(schema = @Schema(implementation = AccountResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccount() {
        List<AccountResponse> accounts = accountService.getAllAccount();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve an account by its ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return ResponseEntity containing the retrieved account response and HTTP status 200
     * @throws AccountNotExistException if the account with the given ID does not exist
     */
    @Operation(summary = "Get account details by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully", content = @Content(schema = @Schema(implementation = AccountResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid account ID", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId)
            throws AccountNotExistException {
        AccountResponse account = accountService.getAccountById(accountId);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    /**
     * Endpoint to deactivate an account by its ID.
     *
     * @param accountId the ID of the account to deactivate
     * @return ResponseEntity with HTTP status 204 indicating successful deactivation
     * @throws AccountNotExistException if the account with the given ID does not exist
     */
    @Operation(summary = "Deactivate account by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid account ID", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PutMapping("/{accountId}/deactivate")
    public ResponseEntity<HttpStatus> deactivateAccount(@PathVariable Long accountId)
            throws AccountNotExistException {
        accountService.deactivateAccountById(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to activate an account by its ID.
     *
     * @param accountId the ID of the account to activate
     * @return ResponseEntity with HTTP status 204 indicating successful activation
     * @throws AccountNotExistException if the account with the given ID does not exist
     */
    @Operation(summary = "Activate account by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid account detail", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PutMapping("/{accountId}/activate")
    public ResponseEntity<HttpStatus> activateAccount(@PathVariable Long accountId)
            throws AccountNotExistException {
        accountService.activateAccountById(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to update account details.
     *
     * @param accountRequest the request body containing updated account details
     * @param accountId      the ID of the account to update
     * @return ResponseEntity containing the updated account response and HTTP status 200
     * @throws AccountNotExistException if the account with the given ID does not exist
     */
    @PutMapping("/{accountId}")
    @Operation(summary = "Update account details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(schema = @Schema(implementation = AccountResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @JsonView(Views.Update.class)
    public ResponseEntity<AccountResponse> updateAccount(@Valid @RequestBody AccountRequest accountRequest, @PathVariable Long accountId) {
        AccountResponse updatedAccount = accountService.updateAccount(accountRequest, accountId);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }
}
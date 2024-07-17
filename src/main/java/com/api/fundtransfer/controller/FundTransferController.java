package com.api.fundtransfer.controller;

import com.api.fundtransfer.dto.FundTransferRequest;
import com.api.fundtransfer.dto.FundTransferResponse;
import com.api.fundtransfer.exception.AccountNotExistException;
import com.api.fundtransfer.exception.BalanceNotSufficientException;
import com.api.fundtransfer.service.FundTransferService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Fund Transfer")
@RestController
@RequestMapping("/fund-transfer")
public class FundTransferController {
    private final FundTransferService fundTransferService;

    @Autowired
    public FundTransferController(FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
    }

    @Operation(summary = "Transfer funds from debit to credit account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fund transfer successful", content = @Content(schema = @Schema(implementation = FundTransferResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity transferFunds(@Valid @RequestBody FundTransferRequest fundTransferRequest) throws AccountNotExistException, BalanceNotSufficientException {
        fundTransferService.transferFunds(fundTransferRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Get all transaction details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of transactions retrieved", content = @Content(schema = @Schema(implementation = FundTransferResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    @GetMapping
    public ResponseEntity<List<FundTransferResponse>> getTransactions() {
        List<FundTransferResponse> transactions = fundTransferService.getTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}

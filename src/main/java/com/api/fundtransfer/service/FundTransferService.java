package com.api.fundtransfer.service;

import com.api.fundtransfer.dto.FundTransferRequest;
import com.api.fundtransfer.dto.FundTransferResponse;

import java.util.List;

public interface FundTransferService {

    /**
     * Transfers funds based on the given fund transfer request.
     *
     * @param fundTransferRequest the fund transfer request DTO
     */
    void transferFunds(FundTransferRequest fundTransferRequest);

    /**
     * Retrieves all fund transfer transactions.
     *
     * @return a list of fund transfer response DTOs
     */
    List<FundTransferResponse> getTransactions();
}

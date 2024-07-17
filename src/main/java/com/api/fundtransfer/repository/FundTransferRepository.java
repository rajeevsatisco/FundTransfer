package com.api.fundtransfer.repository;

import com.api.fundtransfer.entity.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for performing CRUD operations on FundTransfer entities.
 */
public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
}

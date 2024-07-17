package com.api.fundtransfer.repository;

import com.api.fundtransfer.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on Account entities.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountIdAndStatus(Long id, String status);
}

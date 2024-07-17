package com.api.fundtransfer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;
    private Long accountFromId;
    private Long accountToId;
    private BigDecimal amount;
    @JsonFormat(pattern = "DD-MM-YYYY HH:MM:ss")
    private LocalDateTime transactionDate;
}

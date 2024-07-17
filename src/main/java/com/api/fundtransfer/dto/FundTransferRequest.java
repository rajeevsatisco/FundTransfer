package com.api.fundtransfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundTransferRequest {

    @NotNull(message = "Debit account is mandatory")
    @Positive(message = "Debit account must be greater than 0")
    @Schema(description = "Debit account Id", example = "1234567")
    private Long accountFromId;

    @Schema(description = "Credit account Id", example = "5463789")
    @NotNull(message = "Credit account is mandatory")
    @Positive(message = "Credit account must be greater than 0")
    private Long accountToId;

    @Schema(description = "Withdrawal/Debit Amount", example = "345.90")
    @NotNull(message = "Balance is mandatory")
    @PositiveOrZero(message = "Withdrawal amount must be zero or positive")
    @Min(value = 1, message = "Withdrawal amount must be greater than 0")
    private BigDecimal amount;

}

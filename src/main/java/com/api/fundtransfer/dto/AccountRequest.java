package com.api.fundtransfer.dto;

import com.api.fundtransfer.filter.ValidCurrency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    @NotNull(message = "Owner ID cannot be null")
    @Positive(message = "Owner ID must be a positive number")
    @Schema(description = "Account Owner Id", example = "1234567")
    private Long ownerId;

    @NotBlank(message = "Currency is Mandatory")
    @Schema(description = "Currency", example = "EUR")
    @ValidCurrency
    private String currency;

    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance must be zero or positive")
    @Schema(description = "Account Balance Amount", example = "123.00")
    private BigDecimal balance;
}

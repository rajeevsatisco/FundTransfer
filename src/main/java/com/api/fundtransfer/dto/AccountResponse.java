package com.api.fundtransfer.dto;

import com.api.fundtransfer.configuration.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    @JsonView({Views.Create.class,Views.Update.class})
    private Long accountId;
    @Schema(description = "Account Owner Id", example = "1234567")
    @JsonView({Views.Create.class,Views.Update.class})
    private Long ownerId;
    @Schema(description = "Currency", example = "EUR")
    @JsonView({Views.Create.class,Views.Update.class})
    private String currency;
    @Schema(description = "Account Balance Amount", example = "123.00")
    @JsonView({Views.Create.class,Views.Update.class})
    private BigDecimal balance;
    @Schema(description = "Account status", example = "ACTIVE")
    @JsonView({Views.Create.class,Views.Update.class})
    private String status;
    @Schema(description = "Created Date", example = "2023-07-15 12:30:00")
    @JsonView(Views.Create.class)
    private LocalDateTime createdDate;
    @Schema(description = "Last Updated Date", example = "2023-07-15 12:30:00")
    @JsonView({Views.Update.class})
    private LocalDateTime lastUpdatedDate;
}

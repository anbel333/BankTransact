package com.anbel.BankTransact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestWalletDTO {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;

    public String getOperationTypeAsString() {
        return operationType.name();
    }
}

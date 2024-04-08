package com.anbel.BankTransact.controller;

import com.anbel.BankTransact.Exception.InsufficientFundsException;
import com.anbel.BankTransact.Exception.InvalidOperationException;
import com.anbel.BankTransact.Exception.WalletNotFoundException;
import com.anbel.BankTransact.dto.OperationType;
import com.anbel.BankTransact.dto.RequestWalletDTO;
import com.anbel.BankTransact.dto.WalletDTO;
import com.anbel.BankTransact.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {
    @Autowired
    WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletDTO> walletOperation(@RequestBody RequestWalletDTO request) {
        try {
            OperationType operationType = OperationType.valueOf(request.getOperationTypeAsString());
            if (operationType != OperationType.DEPOSIT && operationType != OperationType.WITHDRAW) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            request.setOperationType(operationType);

            WalletDTO response = walletService.walletOperation(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | WalletNotFoundException | InsufficientFundsException |
                 InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDTO> getBalance(@PathVariable UUID walletId) {
        try {
            WalletDTO response = walletService.getWalletBalance(walletId);
            return ResponseEntity.ok(response);
        } catch (WalletNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



}

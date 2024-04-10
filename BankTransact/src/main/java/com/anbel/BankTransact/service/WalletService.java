package com.anbel.BankTransact.service;

import com.anbel.BankTransact.Exception.InsufficientFundsException;
import com.anbel.BankTransact.Exception.InvalidOperationException;
import com.anbel.BankTransact.Exception.WalletNotFoundException;
import com.anbel.BankTransact.dto.OperationType;
import com.anbel.BankTransact.dto.RequestWalletDTO;
import com.anbel.BankTransact.dto.WalletDTO;
import com.anbel.BankTransact.model.Wallet;
import com.anbel.BankTransact.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public WalletDTO walletOperation(RequestWalletDTO request) {
        Wallet wallet = walletRepository.findByIdForUpdate(request.getWalletId())
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setBalance(BigDecimal.ZERO);
                    return newWallet;
                });

        BigDecimal amount = request.getAmount();
        OperationType operationType = request.getOperationType();

        switch (operationType) {
            case DEPOSIT:
                wallet.setBalance(wallet.getBalance().add(amount));
                break;
            case WITHDRAW:
                if(wallet.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException("Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance().subtract(amount));
                break;
            default:
                throw new InvalidOperationException("Invalid operation type");
        }
        walletRepository.save(wallet);
        return new WalletDTO(wallet.getUuid(), wallet.getBalance());
    }
    public WalletDTO getWalletBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        return new WalletDTO(wallet.getUuid(), wallet.getBalance());
    }
}

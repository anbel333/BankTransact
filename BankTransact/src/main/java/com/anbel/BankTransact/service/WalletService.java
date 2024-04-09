package com.anbel.BankTransact.service;

import com.anbel.BankTransact.Exception.InsufficientFundsException;
import com.anbel.BankTransact.Exception.InvalidOperationException;
import com.anbel.BankTransact.Exception.WalletNotFoundException;
import com.anbel.BankTransact.dto.RequestWalletDTO;
import com.anbel.BankTransact.dto.WalletDTO;
import com.anbel.BankTransact.model.Wallet;
import com.anbel.BankTransact.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Mono<WalletDTO> walletOperation(RequestWalletDTO request) {
        return walletRepository.findById(request.getWalletId())
                .defaultIfEmpty(new Wallet())
                .flatMap(wallet -> {
                    BigDecimal amount = request.getAmount();
                    switch (request.getOperationType()) {
                        case DEPOSIT:
                            wallet.setBalance(wallet.getBalance().add(amount));
                            break;
                        case WITHDRAW:
                            if (wallet.getBalance().compareTo(amount) < 0) {
                                return Mono.error(new InsufficientFundsException("Insufficient funds"));
                            }
                            wallet.setBalance(wallet.getBalance().subtract(amount));
                            break;
                        default:
                            return Mono.error(new InvalidOperationException("Invalid operation type"));
                    }
                    return walletRepository.save(wallet);
                })
                .map(wallet -> new WalletDTO(wallet.getUuid(), wallet.getBalance()));
    }

    public Mono<WalletDTO> getWalletBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .switchIfEmpty(Mono.error(new WalletNotFoundException("Wallet not found")))
                .map(wallet -> new WalletDTO(wallet.getUuid(), wallet.getBalance()));
    }
}

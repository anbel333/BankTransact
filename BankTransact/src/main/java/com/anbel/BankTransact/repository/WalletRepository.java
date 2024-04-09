package com.anbel.BankTransact.repository;

import com.anbel.BankTransact.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface WalletRepository extends ReactiveCrudRepository<Wallet, UUID> {
}

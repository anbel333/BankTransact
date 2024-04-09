package com.anbel.BankTransact.service;

import com.anbel.BankTransact.service.WalletService;
import com.anbel.BankTransact.repository.WalletRepository;
import com.anbel.BankTransact.model.Wallet;
import com.anbel.BankTransact.dto.RequestWalletDTO;
import com.anbel.BankTransact.dto.WalletDTO;
import com.anbel.BankTransact.Exception.InsufficientFundsException;
import com.anbel.BankTransact.Exception.WalletNotFoundException;
import com.anbel.BankTransact.dto.OperationType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceTest {
    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    private UUID uuid;
    private Wallet wallet;

    @Before
    public void setUp() {
        uuid = UUID.randomUUID();
        wallet = new Wallet(uuid, new BigDecimal("500"));
    }

    @Test
    public void testWalletOperationDeposit() {
        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.DEPOSIT, new BigDecimal("100"));
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Mono.just(wallet));
        Mockito.when(walletRepository.save(Mockito.any(Wallet.class))).thenAnswer(i -> Mono.just(i.getArgument(0)));

        StepVerifier.create(walletService.walletOperation(requestWalletDTO))
                .assertNext(walletDTO -> {
                    Assert.assertEquals(uuid, walletDTO.getUuid());
                    Assert.assertEquals(0, walletDTO.getAmount().compareTo(new BigDecimal("600")));
                })
                .verifyComplete();
    }

    @Test
    public void testWalletOperationWithdraw() {
        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.WITHDRAW, new BigDecimal("100"));
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Mono.just(wallet));
        Mockito.when(walletRepository.save(Mockito.any(Wallet.class))).thenAnswer(i -> Mono.just(i.getArgument(0)));

        StepVerifier.create(walletService.walletOperation(requestWalletDTO))
                .assertNext(walletDTO -> {
                    Assert.assertEquals(uuid, walletDTO.getUuid());
                    Assert.assertEquals(0, walletDTO.getAmount().compareTo(new BigDecimal("400")));
                })
                .verifyComplete();
    }

    @Test
    public void testWalletOperationInsufficientFunds() {
        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.WITHDRAW, new BigDecimal("600"));
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Mono.just(wallet));

        StepVerifier.create(walletService.walletOperation(requestWalletDTO))
                .expectError(InsufficientFundsException.class)
                .verify();
    }

    @Test
    public void testPerformOperationWalletNotFound() {
        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.WITHDRAW, new BigDecimal("200"));
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Mono.empty());

        StepVerifier.create(walletService.walletOperation(requestWalletDTO))
                .expectError(WalletNotFoundException.class)
                .verify();
    }

    @Test
    public void testGetWalletBalanceWalletNotFound() {
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Mono.empty());

        StepVerifier.create(walletService.getWalletBalance(uuid))
                .expectError(WalletNotFoundException.class)
                .verify();
    }

    @Test
    public void testGetWalletBalance() {
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Mono.just(wallet));

        StepVerifier.create(walletService.getWalletBalance(uuid))
                .assertNext(walletDTO -> {
                    Assert.assertEquals(uuid, walletDTO.getUuid());
                    Assert.assertEquals(0, walletDTO.getAmount().compareTo(new BigDecimal("500")));
                })
                .verifyComplete();
    }
}

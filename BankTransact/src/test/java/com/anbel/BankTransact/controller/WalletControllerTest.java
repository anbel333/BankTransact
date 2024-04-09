package com.anbel.BankTransact.controller;

import com.anbel.BankTransact.controller.WalletController;
import com.anbel.BankTransact.dto.OperationType;
import com.anbel.BankTransact.dto.RequestWalletDTO;
import com.anbel.BankTransact.dto.WalletDTO;
import com.anbel.BankTransact.service.WalletService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @Before
    public void setUp() {
        // Ваши настройки перед каждым тестом
    }

    @Test
    public void testWalletOperationDeposit() {
        UUID uuid = UUID.randomUUID();
        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.DEPOSIT, new BigDecimal("100"));
        WalletDTO walletDTO = new WalletDTO(uuid, new BigDecimal("1000"));

        Mockito.when(walletService.walletOperation(Mockito.any(RequestWalletDTO.class))).thenReturn(Mono.just(walletDTO));

        StepVerifier.create(walletController.walletOperation(Mono.just(requestWalletDTO)))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.OK && walletDTO.equals(response.getBody()))
                .verifyComplete();
    }

    @Test
    public void testGetWalletBalance() {
        UUID uuid = UUID.randomUUID();
        WalletDTO walletDTO = new WalletDTO(uuid, new BigDecimal("1000"));

        Mockito.when(walletService.getWalletBalance(uuid)).thenReturn(Mono.just(walletDTO));

        StepVerifier.create(walletController.getBalance(uuid))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.OK && walletDTO.equals(response.getBody()))
                .verifyComplete();
    }
}

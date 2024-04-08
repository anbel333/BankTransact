package com.anbel.BankTransact.controller;

import com.anbel.BankTransact.dto.OperationType;
import com.anbel.BankTransact.dto.RequestWalletDTO;
import com.anbel.BankTransact.dto.WalletDTO;
import com.anbel.BankTransact.service.WalletService;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class WalletControllerTest {

    @InjectMocks
    WalletController walletController;

    @Mock
    WalletService walletService;

    @Test
    public void testWalletOperationDeposit() {
        UUID uuid = UUID.randomUUID();
        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.DEPOSIT, new BigDecimal("100"));

        WalletDTO walletDTO = new WalletDTO(uuid, new BigDecimal("1000"));

        Mockito.when(walletService.walletOperation(requestWalletDTO)).thenReturn(walletDTO);

        ResponseEntity<WalletDTO> responseEntity = walletController.walletOperation(requestWalletDTO);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(walletDTO, responseEntity.getBody());
    }

    @Test
    public void testWalletOperationWithdraw() {
        UUID uuid = UUID.randomUUID();
        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.WITHDRAW, new BigDecimal("100"));

        WalletDTO walletDTO = new WalletDTO(uuid, new BigDecimal("900"));

        Mockito.when(walletService.walletOperation(requestWalletDTO)).thenReturn(walletDTO);

        ResponseEntity<WalletDTO> responseEntity = walletController.walletOperation(requestWalletDTO);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(walletDTO, responseEntity.getBody());
    }

    @Test
    public void testGetWalletBalance() {
        UUID uuid = UUID.randomUUID();

        WalletDTO walletDTO = new WalletDTO(uuid, new BigDecimal("1000"));

        Mockito.when(walletService.getWalletBalance(uuid)).thenReturn(walletDTO);

        ResponseEntity<WalletDTO> responseEntity = walletController.getBalance(uuid);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(walletDTO, responseEntity.getBody());
    }
}

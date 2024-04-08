package com.anbel.BankTransact.service;

import com.anbel.BankTransact.Exception.InsufficientFundsException;
import com.anbel.BankTransact.Exception.WalletNotFoundException;
import com.anbel.BankTransact.dto.OperationType;
import com.anbel.BankTransact.dto.RequestWalletDTO;
import com.anbel.BankTransact.dto.WalletDTO;
import com.anbel.BankTransact.model.Wallet;
import com.anbel.BankTransact.repository.WalletRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceTest {
    @InjectMocks
    private WalletService walletService;
    @Mock
    private WalletRepository walletRepository;

    @Test
    public void testWalletOperationDeposit() {
        UUID uuid = UUID.randomUUID();
        Wallet wallet = new Wallet(uuid, new BigDecimal("500"));

        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.DEPOSIT, new BigDecimal("100"));
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Optional.of(wallet));

        WalletDTO responseWalletDto = walletService.walletOperation(requestWalletDTO);

        Assert.assertEquals(new BigDecimal("600"), responseWalletDto.getAmount());
    }

    @Test
    public void testWalletOperationWithdraw() {
        UUID uuid = UUID.randomUUID();
        Wallet wallet = new Wallet(uuid, new BigDecimal("500"));

        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.WITHDRAW, new BigDecimal("100"));
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Optional.of(wallet));

        WalletDTO responseWalletDTO = walletService.walletOperation(requestWalletDTO);

        Assert.assertEquals(new BigDecimal("400"), responseWalletDTO.getAmount());
    }

    @Test(expected = InsufficientFundsException.class)
    public void testWalletOperationInsufficientFunds() {
        UUID uuid = UUID.randomUUID();
        Wallet wallet  = new Wallet(uuid, new BigDecimal("100"));

        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.WITHDRAW, new BigDecimal("200"));
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Optional.of(wallet));

        walletService.walletOperation(requestWalletDTO);
    }

    @Test(expected = WalletNotFoundException.class)
    public void testPerformOperation_WalletNotFound() {
        UUID uuid = UUID.randomUUID();

        RequestWalletDTO requestWalletDTO = new RequestWalletDTO(uuid, OperationType.WITHDRAW, new BigDecimal("200"));
        Mockito.when(walletRepository.findById(uuid)).thenReturn(Optional.empty());

        walletService.walletOperation(requestWalletDTO);
    }

    @Test(expected = WalletNotFoundException.class)
    public void testGetWalletBalanceWalletNotFound() {
        UUID uuid = UUID.randomUUID();

        Mockito.when(walletRepository.findById(uuid)).thenReturn(Optional.empty());

        walletService.getWalletBalance(uuid);
    }

    @Test
    public void testGetWalletBalance() {
        UUID uuid = UUID.randomUUID();
        Wallet wallet = new Wallet(uuid, new BigDecimal("1000"));

        Mockito.when(walletRepository.findById(uuid)).thenReturn(Optional.of(wallet));

        WalletDTO walletDTO = walletService.getWalletBalance(uuid);

        Assert.assertEquals(uuid, walletDTO.getUuid());
        Assert.assertEquals(new BigDecimal("1000"), walletDTO.getAmount());
    }

}

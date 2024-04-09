package com.anbel.BankTransact.controller;

import com.anbel.BankTransact.dto.RequestWalletDTO;
import com.anbel.BankTransact.dto.WalletDTO;
import com.anbel.BankTransact.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public Mono<ResponseEntity<WalletDTO>> walletOperation(@RequestBody Mono<RequestWalletDTO> requestMono) {
        return requestMono
                .flatMap(request -> walletService.walletOperation(request))
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    @GetMapping("/{walletId}")
    public Mono<ResponseEntity<WalletDTO>> getBalance(@PathVariable UUID walletId) {
        return walletService.getWalletBalance(walletId)
                .map(response -> ResponseEntity.ok(response))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
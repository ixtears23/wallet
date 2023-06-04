package junseok.snr.wallet.api.controller;


import jakarta.validation.Valid;
import junseok.snr.wallet.api.controller.dto.CreateWalletRequestDto;
import junseok.snr.wallet.api.controller.dto.CreateWalletResponseDto;
import junseok.snr.wallet.api.domain.Wallet;
import junseok.snr.wallet.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/crypto-wallet/v1/wallets")
@RestController
public class WalletController {
    private final WalletService walletService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreateWalletResponseDto createWallet(@RequestBody @Valid CreateWalletRequestDto request) throws Exception {
        log.info(">>>>> createWallet - walletType : {}", request.getWalletType());
        final Wallet wallet = walletService.createWallet(request);

        return CreateWalletResponseDto.builder()
                .id(wallet.getId())
                .walletType(wallet.getWalletType())
                .balance(wallet.getBalance())
                .address(wallet.getAddress())
                .build();
    }

    @GetMapping("/{address}")
    public BigInteger getBalance(@PathVariable String address) throws Exception {
        log.info(">>>>> getBalance - address : {}", address);
        return walletService.getBalance(address);
    }
}

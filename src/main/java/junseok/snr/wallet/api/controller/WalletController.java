package junseok.snr.wallet.api.controller;


import jakarta.validation.Valid;
import junseok.snr.wallet.api.controller.dto.CreateWalletDto;
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
    public Wallet createWallet(@RequestBody @Valid CreateWalletDto request) throws Exception {
        log.info(">>>>> createWallet - walletType : {}", request.getWalletType());
        return walletService.createWallet(request);
    }

    @GetMapping("/{address}")
    public BigInteger getBalance(@PathVariable String address) throws Exception {
        log.info(">>>>> getBalance - address : {}", address);
        return walletService.getBalance(address);
    }
}

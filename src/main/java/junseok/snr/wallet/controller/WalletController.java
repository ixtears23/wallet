package junseok.snr.wallet.controller;


import jakarta.validation.Valid;
import junseok.snr.wallet.controller.dto.CreateWalletDto;
import junseok.snr.wallet.domain.Wallet;
import junseok.snr.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/crypto-wallet/v1/wallets")
@RestController
public class WalletController {
    private final WalletService walletService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public Wallet createWallet(@RequestBody @Valid CreateWalletDto.Request request) throws Exception {
        log.info(">>>>> createWallet - walletType : {}", request.getWalletType());
        return walletService.createWallet(request);
    }

    @GetMapping("/{address}")
    public BigInteger getBalance(@PathVariable String address) throws Exception {
        log.info(">>>>> getBalance - address : {}", address);
        return walletService.getBalance(address);
    }
}

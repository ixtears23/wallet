package junseok.snr.wallet.controller;


import jakarta.validation.Valid;
import junseok.snr.wallet.controller.dto.CreateWalletDto;
import junseok.snr.wallet.domain.Wallet;
import junseok.snr.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/crypto-wallet/v1/wallets")
@RestController
public class WalletController {
    private final WalletService walletService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public Wallet createWallet(@RequestBody @Valid CreateWalletDto createWalletDto) throws Exception {
        log.info(">>>>> createWallet - walletType : {}", createWalletDto.getWalletType());
        return walletService.createWallet(createWalletDto);
    }
}

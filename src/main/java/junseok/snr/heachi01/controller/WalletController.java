package junseok.snr.heachi01.controller;

import junseok.snr.heachi01.domain.EthereumWallet;
import junseok.snr.heachi01.dto.CreateEthereumWallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class WalletController {

    @PostMapping(value ="/wallet")
    public EthereumWallet createEthereumWallet(@RequestBody final CreateEthereumWallet createEthereumWallet) throws Exception {
        return new EthereumWallet(createEthereumWallet.getPassword());
    }

}
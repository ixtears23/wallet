package junseok.snr.heachi01.controller;

import junseok.snr.heachi01.domain.EthereumWallet;
import junseok.snr.heachi01.dto.CreateEthereumWallet;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

@Slf4j
@RestController
public class WalletController {
    private static final Web3j web3j = Web3j.build(new HttpService("https://tn.henesis.io/ethereum/goerli?clientId=815fcd01324b8f75818a755a72557750"));


    @PostMapping(value ="/wallet")
    public EthereumWallet createEthereumWallet(@RequestBody final CreateEthereumWallet createEthereumWallet) throws Exception {

        final OkHttpClient okHttpClient = new OkHttpClient();


        return new EthereumWallet(createEthereumWallet.getPassword());
    }

    @GetMapping(value = "/wallet/balance/{address}")
    public static BigInteger getBalance(@PathVariable String address) {
        log.info(">>>>> getBalance address :: {}", address);

        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            return ethGetBalance.getBalance();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching balance", e);
        }
    }

    public static void main(String[] args) {
        final BigInteger balance = WalletController.getBalance("0x47567102B073c29621B06cDCaDF9754ed78F4129");
        log.info(">>>>> balance ::: {}", balance);
    }
}
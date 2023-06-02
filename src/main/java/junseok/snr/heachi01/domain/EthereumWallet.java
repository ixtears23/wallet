package junseok.snr.heachi01.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Getter
@ToString
public class EthereumWallet {
    private final String publicKey;
    private final String privateKey;
    private final String address;

    static Web3j web3j = Web3j.build(new HttpService("https://tn.henesis.io/ethereum/goerli?clientId=815fcd01324b8f75818a755a72557750"));

    public EthereumWallet(final String password) throws Exception {
        final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        final String walletFile = WalletUtils.generateWalletFile(password, ecKeyPair, new File("./"), true);
        log.info(">>>>> walletFile :: {}", walletFile);

        final Credentials credentials = Credentials.create(ecKeyPair);
        address = credentials.getAddress();
        privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        publicKey = credentials.getEcKeyPair().getPublicKey().toString(16);

        log.info(">>>>> Address :: {}", credentials.getAddress());
        log.info(">>>>> Public Key :: {}", credentials.getEcKeyPair());
        log.info(">>>>> Private Key :: {}", credentials.getAddress());
    }

    public static void main(String[] args) throws Exception {
        final EthereumWallet ethereumWallet = new EthereumWallet("1234qwer");

        final CompletableFuture<String> transfer = transfer("0x47567102b073c29621b06cdcadf9754ed78f4129", "0x559c2F8E4e2ab7BCDfE05788fB21c777C4659045", BigDecimal.valueOf(1000));


        System.out.println(transfer.get());
    }

    public static CompletableFuture<String> transfer(@RequestParam("privateKey") String privateKey,
                                                     @RequestParam("toAddress") String toAddress,
                                                     @RequestParam("amount") BigDecimal amount) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        return Transfer.sendFunds(web3j, credentials, toAddress,
                        amount, Convert.Unit.ETHER)
                .sendAsync()
                .thenApply(TransactionReceipt::getTransactionHash);
    }

}

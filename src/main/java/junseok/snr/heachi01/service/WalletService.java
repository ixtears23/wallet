package junseok.snr.heachi01.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

@Service
public class WalletService {
    private final Web3j web3j;

    @Autowired
    public WalletService(@Value("${ethereum.endpoint}") String ethereumEndpoint) {
        this.web3j = Web3j.build(new HttpService(ethereumEndpoint));
    }

    public WalletFile createWallet(String password) throws Exception {
        ECKeyPair keyPair = Keys.createEcKeyPair();
        return Wallet.createStandard(password, keyPair);
    }

    public Credentials loadCredentials(String password, WalletFile walletFile) throws CipherException {
        return Credentials.create(Wallet.decrypt(password, walletFile));
    }

}

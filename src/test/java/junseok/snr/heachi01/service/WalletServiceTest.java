package junseok.snr.heachi01.service;

import org.junit.jupiter.api.Test;
import org.web3j.crypto.WalletFile;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceTest {

    private static final WalletService walletSErvice = new WalletService("https://tn.henesis.io/ethereum/goerli");

    @Test
    void createWalletTest() {
        walletSErvice.createWallet();
    }


    @Test
    void createWalletTest2() throws Exception {
        final WalletFile wallet = walletSErvice.createWallet("1234qwer");

    }
}
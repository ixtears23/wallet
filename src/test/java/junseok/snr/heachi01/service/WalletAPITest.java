package junseok.snr.heachi01.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletAPITest {

    @Test
    void createWallet() throws Exception {
        final WalletAPI walletAPI = new WalletAPI();
        walletAPI.createWallet();
    }
}
package junseok.snr.heachi01.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.WalletFile;

class WalletServiceTest {

    private static final WalletService walletSErvice = new WalletService("https://tn.henesis.io/ethereum/goerli");


    @DisplayName("지갑 생성 성공")
    @Test
    void createWalletSuccessTest() throws Exception {
        final WalletFile wallet = walletSErvice.createWallet("1234qwer");
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getId());
        System.out.println(wallet.getVersion());
        System.out.println(wallet.getCrypto());
    }

}
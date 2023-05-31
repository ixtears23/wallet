package junseok.snr.heachi01.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EthereumWalletTest {

    @DisplayName("")
    @Test
    void createEthereumWalletTest() throws Exception {
        final String password = "1234qwer";

        final EthereumWallet ethereumWallet = new EthereumWallet(password);

        final String address = ethereumWallet.getAddress();
        final String publicKey = ethereumWallet.getPublicKey();
        final String privateKey = ethereumWallet.getPrivateKey();

        System.out.println("address : " + address);
        System.out.println("publicKey : " + publicKey);
        System.out.println("privateKey : " + privateKey);

    }
}
package junseok.snr.heachi02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletUtilsTest {

    @Test
    void getPrivateKey() throws Exception {
        final String privateKey = WalletUtils.getPrivateKey("./UTC--2023-06-02T02-21-22.50348000Z--47567102b073c29621b06cdcadf9754ed78f4129.json");

        System.out.println(privateKey);
    }
}
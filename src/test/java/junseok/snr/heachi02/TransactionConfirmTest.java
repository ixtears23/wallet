package junseok.snr.heachi02;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TransactionConfirmTest {

    @Test
    void sendTransaction() throws Exception {
        final String privateKey = "60500566a9947493d5edff5039461ce26d5080536bd36625a4bf61a4b07bd41b";
        final String to = "0x9073f43E9bb99C0BB848dFb713eC452C702e7ceD";
        final BigInteger etherInWei = BigInteger.valueOf(100);

        final TransactionConfirm transactionConfirm = new TransactionConfirm();
        transactionConfirm.sendTransaction(privateKey, to, etherInWei);

    }

    @Test
    void getGasWithLimitTest() {
        final TransactionConfirm transactionConfirm = new TransactionConfirm();
        transactionConfirm.getGasLimit();
        transactionConfirm.getGasPrice();
    }
}
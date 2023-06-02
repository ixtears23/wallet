package junseok.snr.heachi02;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TransactionConfirmTest {
    private static TransactionConfirm transactionConfirm;

    @BeforeAll
    static void setUp() {
        transactionConfirm = new TransactionConfirm();
    }

    @Test
    void getConfirmationNumberTest() throws Exception {
        transactionConfirm.getConfirmationNumber("0xc70cd15d247b7f4b3cd02b3e48964346c2d36113ccddc884dfc0288dfb9fad1f");
        transactionConfirm.getConfirmationNumber("0xea934e587ab6886e60e8ce87ab93cbba45eb1ae4097c50ba59e728134bc248db");
    }


    @Test
    void sendTransaction() throws Exception {
        final String privateKey = "60500566a9947493d5edff5039461ce26d5080536bd36625a4bf61a4b07bd41b";
        final String to = "0x9073f43E9bb99C0BB848dFb713eC452C702e7ceD";
        final BigInteger etherInWei = BigInteger.valueOf(100_000_000_000_000L);

        transactionConfirm.sendTransaction(privateKey, to, etherInWei);

    }

    @Test
    void getGasWithLimitTest() {
        transactionConfirm.getGasLimit();
        transactionConfirm.getGasPrice();
    }
}
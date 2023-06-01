package junseok.snr.heachi01.service;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class BalanceTest {

    @Test
    void getBalanceTest() throws Exception {
        final Balance balanceApp = new Balance();
        final BigInteger balance = balanceApp.balance("9c113ad12691fed1459c77bca5212b8a24bed2f7");

        System.out.println("balance : " + balance);
    }
}
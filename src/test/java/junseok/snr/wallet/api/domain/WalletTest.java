package junseok.snr.wallet.api.domain;

import junseok.snr.wallet.api.service.WalletException;
import junseok.snr.wallet.domain.Wallet;
import junseok.snr.wallet.domain.WalletType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet("address", "password", "privateKey", WalletType.ETH);
    }

    @DisplayName("입금한 금액이 잔액에 제대로 들어가는지 테스트")
    @Test
    void depositTest() {
        BigDecimal initialBalance = wallet.getBalance();
        BigDecimal depositAmount = new BigDecimal("100");

        wallet.deposit(depositAmount);

        Assertions.assertEquals(initialBalance.add(depositAmount), wallet.getBalance());
    }

    @DisplayName("충분한 잔액이 있을 때 출금이 제대로 되는지 테스트")
    @Test
    void withdrawEnoughBalanceTest() {
        BigDecimal depositAmount = new BigDecimal("200");
        wallet.deposit(depositAmount);

        BigDecimal withdrawAmount = new BigDecimal("100");
        wallet.withdraw(withdrawAmount);

        Assertions.assertEquals(depositAmount.subtract(withdrawAmount), wallet.getBalance());
    }

    @DisplayName("잔액이 충분하지 않을 때 출금하면 WalletException이 발생하는지 테스트")
    @Test
    void withdrawInsufficientBalanceTest() {
        BigDecimal withdrawAmount = new BigDecimal("300");

        Assertions.assertThrows(WalletException.class, () -> wallet.withdraw(withdrawAmount));
    }

    @DisplayName("충분한 잔액이 있을 때 isWithdrawalImpossible 메서드가 false 를 반환하는지 테스트")
    @Test
    void isWithdrawalImpossibleEnoughBalanceTest() {
        BigDecimal depositAmount = new BigDecimal("200");

        wallet.deposit(depositAmount);

        Assertions.assertFalse(wallet.isWithdrawalImpossible(depositAmount));
    }

    @DisplayName("잔액이 부족할 때 isWithdrawalImpossible 메서드가 true 를 반환하는지 테스트")
    @Test
    void isWithdrawalImpossibleInsufficientBalanceTest() {
        BigDecimal depositAmount = new BigDecimal("100");

        wallet.deposit(depositAmount);

        Assertions.assertTrue(wallet.isWithdrawalImpossible(depositAmount.add(BigDecimal.ONE)));
    }
}
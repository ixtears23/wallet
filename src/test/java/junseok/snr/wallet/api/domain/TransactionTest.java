package junseok.snr.wallet.api.domain;

import static org.junit.jupiter.api.Assertions.*;

import junseok.snr.wallet.application.service.ExceptionCode;
import junseok.snr.wallet.application.service.TransactionException;
import junseok.snr.wallet.application.service.WalletException;
import junseok.snr.wallet.domain.Transaction;
import junseok.snr.wallet.domain.TransactionStatus;
import junseok.snr.wallet.domain.TransactionType;
import junseok.snr.wallet.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

class TransactionTest {
    private Wallet wallet;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        wallet = Mockito.mock(Wallet.class);
    }

    @DisplayName("Transaction 생성 시, wallet이 null인 경우 TransactionException 발생, ExceptionCode.TRN_001 에러 코드 반환")
    @Test
    void createTransactionWithNullWalletTest() {
        Exception exception = assertThrows(TransactionException.class, () -> {
            transaction = new Transaction(null, "testHash", new BigDecimal(10), TransactionType.WITHDRAW);
        });

        String expectedMessage = ExceptionCode.TRN_001.getMessage();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Transaction 생성 시, 출금이 불가능한 경우 WalletException 발생, ExceptionCode.TRN_002 에러 코드 반환")
    @Test
    void createTransactionWithWithdrawalImpossibleTest() {
        Mockito.when(wallet.isWithdrawalImpossible(Mockito.any(BigDecimal.class)))
                .thenReturn(true);

        Exception exception = assertThrows(WalletException.class, () -> {
            transaction = new Transaction(wallet, "testHash", new BigDecimal(10), TransactionType.WITHDRAW);
        });

        String expectedMessage = ExceptionCode.TRN_002.getMessage();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Transaction 생성 시 wallet 이 null이 아니고, 출금이 불가능하지 않은 경우 Transaction 생성 성공")
    @Test
    void createTransactionSuccessTest() {
        Mockito.when(wallet.isWithdrawalImpossible(Mockito.any(BigDecimal.class)))
                .thenReturn(false);

        transaction = new Transaction(wallet, "testHash", new BigDecimal(10), TransactionType.WITHDRAW);

        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
        assertEquals(wallet, transaction.getWallet());
        assertEquals("testHash", transaction.getTransactionHash());
        assertEquals(new BigDecimal(10), transaction.getAmount());
        assertEquals(TransactionType.WITHDRAW, transaction.getType());
    }

    @DisplayName("Transaction 생성 시 ConfirmCount가 0보다 큰 경우 MINED 상태로 변경되는 테스트")
    @Test
    void updateStatusLessThanConfirmCountTest() {
        Mockito.when(wallet.isWithdrawalImpossible(Mockito.any(BigDecimal.class)))
                .thenReturn(false);
        transaction = new Transaction(wallet, "testHash", new BigDecimal(10), TransactionType.WITHDRAW);

        transaction.updateStatus(10);

        assertEquals(TransactionStatus.MINED, transaction.getStatus());
    }

    @DisplayName("Transaction(출금) 생성 시 ConfirmCount가 12보다 큰 경우 CONFIRMED 상태로 변경되는 테스트")
    @Test
    void updateStatusGreaterThanConfirmCountTest() {
        Mockito.when(wallet.isWithdrawalImpossible(Mockito.any(BigDecimal.class)))
                .thenReturn(false);
        transaction = new Transaction(wallet, "testHash", new BigDecimal(10), TransactionType.WITHDRAW);

        transaction.updateStatus(15);

        assertEquals(TransactionStatus.CONFIRMED, transaction.getStatus());
        Mockito.verify(wallet, Mockito.times(1))
                .withdraw(Mockito.any(BigDecimal.class));
    }

    @DisplayName("Transaction(입금) 생성 시 ConfirmCount가 12보다 큰 경우 CONFIRMED 상태로 변경되는 테스트")
    @Test
    void updateStatusWithDepositTest() {
        Mockito.when(wallet.isWithdrawalImpossible(Mockito.any(BigDecimal.class)))
                .thenReturn(false);
        transaction = new Transaction(wallet, "testHash", new BigDecimal(10), TransactionType.DEPOSIT);

        transaction.updateStatus(15);

        assertEquals(TransactionStatus.CONFIRMED, transaction.getStatus());
        Mockito.verify(wallet, Mockito.times(1))
                .deposit(Mockito.any(BigDecimal.class));
    }
}
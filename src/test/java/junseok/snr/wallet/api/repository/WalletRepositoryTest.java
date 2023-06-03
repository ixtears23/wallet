package junseok.snr.wallet.api.repository;

import junseok.snr.wallet.api.domain.Transaction;
import junseok.snr.wallet.api.domain.TransactionType;
import junseok.snr.wallet.api.domain.Wallet;
import junseok.snr.wallet.api.domain.WalletType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WalletRepositoryTest {
    @Autowired WalletRepository walletRepository;
    @Autowired TransactionRepository transactionRepository;

    @Test
    void nothing() {

        final Wallet wallet = new Wallet(
                "1234qwer",
                "1234qwer",
                "privatekey",
                WalletType.ETH
        );

        walletRepository.save(wallet);

        final Wallet findWallet = walletRepository.findByAddress("1234qwer");

        final Transaction trn = transactionRepository.save(
                new Transaction(
                        findWallet,
                        "hash",
                        BigDecimal.ONE,
                        TransactionType.WITHDRAW
                )
        );

        final Transaction transaction = transactionRepository.findById(trn.getId())
                .orElse(null);

    }

}
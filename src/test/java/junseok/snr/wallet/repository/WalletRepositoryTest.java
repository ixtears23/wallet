package junseok.snr.wallet.repository;

import junseok.snr.wallet.api.domain.Wallet;
import junseok.snr.wallet.api.domain.WalletType;
import junseok.snr.wallet.api.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@SpringBootTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void testSaveWallet() {
        final Wallet wallet = new Wallet("test", "password", "pk", WalletType.ETH);
        final Wallet savedWallet = walletRepository.save(wallet);

        final Wallet findWallet = walletRepository.findById(savedWallet.getId())
                .orElse(null);
    }

}
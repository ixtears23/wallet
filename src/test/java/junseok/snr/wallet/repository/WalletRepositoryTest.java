package junseok.snr.wallet.repository;

import junseok.snr.wallet.domain.Wallet;
import junseok.snr.wallet.domain.WalletType;
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
        final Wallet wallet = new Wallet("test", "pk", WalletType.ETH);
        final Wallet savedWallet = walletRepository.save(wallet);

        final Wallet findWallet = walletRepository.findById(savedWallet.getId())
                .orElse(null);

        assertThat(findWallet).isEqualTo(savedWallet);
    }

}
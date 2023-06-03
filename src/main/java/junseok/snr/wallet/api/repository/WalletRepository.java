package junseok.snr.wallet.api.repository;

import junseok.snr.wallet.api.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}

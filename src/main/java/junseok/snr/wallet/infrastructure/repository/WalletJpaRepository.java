package junseok.snr.wallet.infrastructure.repository;

import junseok.snr.wallet.domain.model.Wallet;
import junseok.snr.wallet.domain.repository.WalletRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletJpaRepository extends JpaRepository<Wallet, Long>, WalletRepository {
}

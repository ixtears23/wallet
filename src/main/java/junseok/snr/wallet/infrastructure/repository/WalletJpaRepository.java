package junseok.snr.wallet.infrastructure.repository;

import junseok.snr.wallet.domain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletJpaRepository extends JpaRepository<Wallet, Long> {
    Wallet findByAddress(String address);
}
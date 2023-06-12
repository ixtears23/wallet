package junseok.snr.wallet.domain.repository;

import junseok.snr.wallet.domain.model.Wallet;

import java.util.List;

public interface WalletRepository {
    Wallet findByAddress(String address);
    List<Wallet> findAll();
    Wallet save(Wallet wallet);
}

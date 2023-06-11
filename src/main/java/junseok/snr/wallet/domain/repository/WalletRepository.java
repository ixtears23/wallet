package junseok.snr.wallet.domain.repository;

import junseok.snr.wallet.domain.model.Wallet;

public interface WalletRepository {
    Wallet findByAddress(String address);
}

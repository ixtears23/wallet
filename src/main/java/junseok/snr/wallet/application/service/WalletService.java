package junseok.snr.wallet.application.service;

import junseok.snr.wallet.api.dto.CreateWalletRequestDto;
import junseok.snr.wallet.domain.model.Wallet;

import java.math.BigInteger;

public interface WalletService {
    Wallet createWallet(CreateWalletRequestDto request) throws Exception;
    BigInteger getBalance(String address) throws Exception;
}

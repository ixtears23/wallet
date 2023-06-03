package junseok.snr.wallet.api.service;

import junseok.snr.wallet.api.controller.dto.CreateWalletDto;
import junseok.snr.wallet.api.domain.Wallet;

import java.math.BigInteger;

public interface WalletService {
    Wallet createWallet(CreateWalletDto request) throws Exception;
    BigInteger getBalance(String address) throws Exception;
}

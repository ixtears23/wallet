package junseok.snr.wallet.api.service;

import junseok.snr.wallet.api.controller.dto.CreateWalletRequestDto;
import junseok.snr.wallet.api.domain.Wallet;

import java.math.BigInteger;

public interface WalletService {
    Wallet createWallet(CreateWalletRequestDto request) throws Exception;
    BigInteger getBalance(String address) throws Exception;
}

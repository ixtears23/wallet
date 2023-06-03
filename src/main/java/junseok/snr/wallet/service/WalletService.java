package junseok.snr.wallet.service;

import junseok.snr.wallet.controller.dto.CreateWalletDto;
import junseok.snr.wallet.domain.Wallet;

import java.math.BigInteger;

public interface WalletService {
    Wallet createWallet(CreateWalletDto.Request createWalletDto) throws Exception;
    BigInteger getBalance(String address) throws Exception;
}

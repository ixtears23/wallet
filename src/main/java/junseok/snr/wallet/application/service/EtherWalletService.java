package junseok.snr.wallet.application.service;

import io.micrometer.common.util.StringUtils;
import junseok.snr.wallet.application.service.exception.ExceptionCode;
import junseok.snr.wallet.application.service.exception.WalletException;
import junseok.snr.wallet.infrastructure.common.Web3jUtils;
import junseok.snr.wallet.api.dto.CreateWalletRequestDto;
import junseok.snr.wallet.domain.model.Wallet;
import junseok.snr.wallet.domain.model.WalletType;
import junseok.snr.wallet.infrastructure.repository.WalletJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class EtherWalletService implements WalletService {
    private final WalletJpaRepository walletJpaRepository;
    private final Web3jUtils web3jUtils;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Wallet createWallet(CreateWalletRequestDto request) throws Exception {
        final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        final String password = request.getPassword();

        final String walletFile = generateWalletFile(ecKeyPair, password);
        log.info(">>>>> walletFile :: {}", walletFile);
        return walletJpaRepository.save(createWallet(ecKeyPair, password));
    }

    private Wallet createWallet(ECKeyPair ecKeyPair, String password) {
        final Credentials credentials = Credentials.create(ecKeyPair);
        final String address = credentials.getAddress();
        final String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

        log.info(">>>>> Address :: {}", credentials.getAddress());

        return new Wallet(address, password, privateKey, WalletType.ETH);
    }

    private String generateWalletFile(ECKeyPair ecKeyPair, String password) throws CipherException, IOException {
        return WalletUtils.generateWalletFile(
                password,
                ecKeyPair,
                new File("./"),
                true);
    }

    @Transactional(readOnly = true)
    @Override
    public BigInteger getBalance(String address) throws Exception {
        if (StringUtils.isBlank(address)) throw new WalletException(ExceptionCode.WAL_001);

        final CompletableFuture<EthGetBalance> future = web3jUtils.getWeb3j()
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync();
        final EthGetBalance ethGetBalance = future.get();
        final BigInteger balance = ethGetBalance.getBalance();
        log.info(">>>>> address : {}, ethGetBalance :: {}", address, balance);
        final Wallet wallet = walletJpaRepository.findByAddress(address);
        final BigInteger walletBalance = wallet.getBalance().toBigInteger();
        log.info(">>>>> address : {}, walletBalance :: {}", address, walletBalance);

        return walletBalance;
    }

    public BigDecimal getBalanceInEther(final String address) throws Exception {
        BigInteger balanceInWei = getBalance(address);

        // Convert the balance from Wei to Ether
        final BigDecimal balanceInEther = web3jUtils.convertToEther(balanceInWei);
        log.info(">>>>> address : {}, balanceInEther :: {}", address, balanceInEther);
        return balanceInEther;
    }
}

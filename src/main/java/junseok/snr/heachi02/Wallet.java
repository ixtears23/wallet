package junseok.snr.heachi02;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class Wallet {

    public BigInteger getBalance(String address) throws Exception {
        if (StringUtils.isBlank(address)) throw new BalanceException("주소 없이 잔액조회를 할 수 없습니다.");

        final CompletableFuture<EthGetBalance> future = WalletUtils.getWeb3j()
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync();
        final EthGetBalance ethGetBalance = future.get();
        final BigInteger balance = ethGetBalance.getBalance();

        log.info(">>>>> address : {}, balanceInWei :: {}", address, balance);

        return balance;
    }

    public BigDecimal getBalanceInEther(final String address) throws Exception {
        BigInteger balanceInWei = getBalance(address);

        // Convert the balance from Wei to Ether
        final BigDecimal balanceInEther = WalletUtils.convertToEther(balanceInWei);
        log.info(">>>>> address : {}, balanceInEther :: {}", address, balanceInEther);
        return balanceInEther;
    }

}

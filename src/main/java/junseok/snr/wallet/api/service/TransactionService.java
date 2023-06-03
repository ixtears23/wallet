package junseok.snr.wallet.api.service;

import junseok.snr.wallet.Web3jUtils;
import junseok.snr.wallet.api.domain.Transaction;
import junseok.snr.wallet.api.domain.TransactionStatus;
import junseok.snr.wallet.api.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@AllArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final Web3jUtils web3jUtils;

    public int getConfirmationNumber(String transactionHash) throws Exception {
        final EthGetTransactionReceipt receiptResponse = web3jUtils.getWeb3j()
                .ethGetTransactionReceipt(transactionHash)
                .sendAsync()
                .get();

        final EthBlockNumber latestBlockResponse = web3jUtils.getWeb3j()
                .ethBlockNumber()
                .sendAsync()
                .get();

        final TransactionReceipt receipt = receiptResponse.getTransactionReceipt()
                .orElseThrow(() -> new RuntimeException("Transaction receipt not generated yet"));
        final BigInteger transactionBlockNumber = receipt.getBlockNumber();

        log.info(">>>>> transactionBlockNumber : {}", transactionBlockNumber);

        BigInteger latestBlockNumber = latestBlockResponse.getBlockNumber();
        final int confirmationNumber = latestBlockNumber.subtract(transactionBlockNumber)
                .intValue();
        log.info(">>>>> transactionHash : {}, confirmationNumber : {}", transactionHash, confirmationNumber);
        return confirmationNumber;
    }

    public String getTransaction(String privateKey, String to, BigInteger etherInWei) throws Exception {
        Credentials credentials = Credentials.create(privateKey);

        BigInteger nonce = web3jUtils.getWeb3j()
                .ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get()
                .getTransactionCount();

        log.info(">>>>> nonce : {}", nonce);

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, getGasPrice(), getGasLimit(), to, etherInWei);
        log.info(">>>>> rawTransaction : {}", rawTransaction);

        // 트랜잭션 서명
        String hexValue = signTransaction(credentials, rawTransaction);

        // 트랜잭션 브로드캐스트
        EthSendTransaction transactionResponse = sendRawTransaction(hexValue);
        log.info(">>>>> transactionResponse : {}", transactionResponse);

        if (transactionResponse.hasError()) {
            throw new Exception("Error processing transaction request: " + transactionResponse.getError().getMessage());
        }

        final String transactionHash = transactionResponse.getTransactionHash();
        log.info(">>>>> transactionHash : {}", transactionHash);

        return transactionHash;
    }

    private EthSendTransaction sendRawTransaction(String hexValue) throws InterruptedException, ExecutionException {
        return web3jUtils.getWeb3j()
                .ethSendRawTransaction(hexValue)
                .sendAsync()
                .get();
    }

    private String signTransaction(Credentials credentials, RawTransaction rawTransaction) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        log.info(">>>>> signedMessage : {}", signedMessage);
        log.info(">>>>> hexValue : {}", hexValue);
        return hexValue;
    }

    public BigInteger getGasPrice() {
        try {
            final BigInteger gasPrice = web3jUtils.getWeb3j()
                    .ethGasPrice()
                    .send()
                    .getGasPrice();

            log.info(">>>>> gasPrice : {}", gasPrice);
            log.info(">>>>> gasPriceInEther : {}", web3jUtils.convertToEther(gasPrice));

            return gasPrice;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigInteger getGasLimit() {
        final BigInteger gasLimit = BigInteger.valueOf(21000);
        log.info(">>>>> gasLimit : {}", gasLimit);
        log.info(">>>>> gasLimitInEther : {}", web3jUtils.convertToEther(gasLimit));
        return gasLimit;
    }

    public List<Transaction> getTransactions(String startingAfter, String endingBefore) {
        if (startingAfter != null) {
            return transactionRepository.findTop10ByTransactionHashGreaterThanOrderByTransactionHashAsc(startingAfter);
        } else if (endingBefore != null) {
            return transactionRepository.findTop10ByTransactionHashLessThanOrderByTransactionHashDesc(endingBefore);
        }
        return transactionRepository.findTop10ByOrderByTransactionHashDesc();
    }
}

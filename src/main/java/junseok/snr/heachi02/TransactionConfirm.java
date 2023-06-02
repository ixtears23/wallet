package junseok.snr.heachi02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class TransactionConfirm {

    public int getConfirmationNumber(String transactionHash) throws Exception {

        EthGetTransactionReceipt receiptResponse = WalletUtils.getWeb3j()
                .ethGetTransactionReceipt(transactionHash)
                .sendAsync()
                .get();

        EthBlockNumber latestBlockResponse = WalletUtils.getWeb3j()
                .ethBlockNumber()
                .sendAsync()
                .get();

        if (receiptResponse.getTransactionReceipt().isPresent()) {
            TransactionReceipt receipt = receiptResponse.getTransactionReceipt().get();
            BigInteger transactionBlockNumber = receipt.getBlockNumber();
            BigInteger latestBlockNumber = latestBlockResponse.getBlockNumber();
            final int confirmationNumber = latestBlockNumber.subtract(transactionBlockNumber).intValue();
            log.info(">>>>> transactionHash : {}, confirmationNumber : {}", transactionHash, confirmationNumber);
            return confirmationNumber;
        } else {
            throw new Exception("Transaction receipt not generated yet");
        }
    }

    public BigInteger getGasPrice() {
        try {
            final BigInteger gasPrice = WalletUtils.getWeb3j()
                    .ethGasPrice()
                    .send()
                    .getGasPrice();

            log.info(">>>>> gasPrice : {}", gasPrice);
            log.info(">>>>> gasPriceInEther : {}", WalletUtils.convertToEther(gasPrice));

            return gasPrice;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigInteger getGasLimit() {
        final BigInteger gasLimit = BigInteger.valueOf(21000);
        log.info(">>>>> gasLimit : {}", gasLimit);
        log.info(">>>>> gasLimitInEther : {}", WalletUtils.convertToEther(gasLimit));
        return gasLimit;
    }

    public String sendTransaction(String privateKey, String to, BigInteger etherInWei) throws Exception {
        Credentials credentials = Credentials.create(privateKey);

        // 트랜잭션 생성
        BigInteger nonce = WalletUtils.getWeb3j().ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();

        log.info(">>>>> nonce : {}", nonce);

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, getGasPrice(), getGasLimit(), to, etherInWei);

        log.info(">>>>> rawTransaction : {}", rawTransaction);

        // 트랜잭션 서명
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        log.info(">>>>> signedMessage : {}", signedMessage);
        log.info(">>>>> hexValue : {}", hexValue);

        // 트랜잭션 브로드캐스트
        EthSendTransaction transactionResponse = WalletUtils.getWeb3j()
                .ethSendRawTransaction(hexValue)
                .sendAsync()
                .get();

        log.info(">>>>> transactionResponse : {}", transactionResponse);

        if (transactionResponse.hasError()) {
            throw new Exception("Error processing transaction request: " + transactionResponse.getError().getMessage());
        }

        final String transactionHash = transactionResponse.getTransactionHash();
        log.info(">>>>> transactionHash : {}", transactionHash);

        return transactionHash;
    }



    public static CompletableFuture<String> transfer(@RequestParam("privateKey") String privateKey,
                                                     @RequestParam("toAddress") String toAddress,
                                                     @RequestParam("amount") BigDecimal amount) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        return Transfer.sendFunds(WalletUtils.getWeb3j(), credentials, toAddress,
                        amount, Convert.Unit.ETHER)
                .sendAsync()
                .thenApply(TransactionReceipt::getTransactionHash);
    }


}

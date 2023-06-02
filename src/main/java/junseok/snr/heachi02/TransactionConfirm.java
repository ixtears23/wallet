package junseok.snr.heachi02;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class TransactionConfirm {


    public static int getConfirmationNumber(String transactionHash) throws Exception {
        EthGetTransactionReceipt receiptResponse = EthNode.getWeb3j().ethGetTransactionReceipt(transactionHash).sendAsync().get();
        EthBlockNumber latestBlockResponse = EthNode.getWeb3j().ethBlockNumber().sendAsync().get();

        if (receiptResponse.getTransactionReceipt().isPresent()) {
            TransactionReceipt receipt = receiptResponse.getTransactionReceipt().get();
            BigInteger transactionBlockNumber = receipt.getBlockNumber();
            BigInteger latestBlockNumber = latestBlockResponse.getBlockNumber();
            return latestBlockNumber.subtract(transactionBlockNumber).intValue();
        } else {
            throw new Exception("Transaction receipt not generated yet");
        }
    }

    public BigInteger getGasPrice() {
        try {
            return EthNode.getWeb3j().ethGasPrice()
                    .send()
                    .getGasPrice();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigInteger getGasLimit() {
        return BigInteger.valueOf(21000);
    }


    public String sendTransaction() throws Exception {
        Credentials credentials = Credentials.create("0x47567102B073c29621B06cDCaDF9754ed78F4129");

        // 트랜잭션 생성
        BigInteger nonce = EthNode.getWeb3j().ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, BigInteger.valueOf(0), BigInteger.valueOf(0), "0x9073f43E9bb99C0BB848dFb713eC452C702e7ceD", BigInteger.valueOf(1_000_000_000_000_000L));

        // 트랜잭션 서명
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        // 트랜잭션 브로드캐스트
        EthSendTransaction transactionResponse = EthNode.getWeb3j().ethSendRawTransaction(hexValue).sendAsync().get();

        if (transactionResponse.hasError()) {
            throw new Exception("Error processing transaction request: " + transactionResponse.getError().getMessage());
        }

        return transactionResponse.getTransactionHash();
    }

    public static void main(String[] args) {
        final TransactionConfirm transactionConfirm = new TransactionConfirm();
        final BigInteger gasPrice = transactionConfirm.getGasPrice();
        final BigInteger gasLimit = transactionConfirm.getGasLimit();
        log.info(">>>>> gasPrice :: {}", gasPrice);
        log.info(">>>>> gasLimit :: {}", gasLimit);
    }


}

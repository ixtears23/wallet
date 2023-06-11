package junseok.snr.wallet.application.service;

import jakarta.persistence.criteria.Predicate;
import junseok.snr.wallet.common.Web3jUtils;
import junseok.snr.wallet.api.controller.dto.WithdrawDto;
import junseok.snr.wallet.domain.Transaction;
import junseok.snr.wallet.domain.TransactionType;
import junseok.snr.wallet.domain.Wallet;
import junseok.snr.wallet.api.repository.TransactionRepository;
import junseok.snr.wallet.api.repository.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Slf4j
@AllArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
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
                .orElseThrow(() -> new TransactionException(ExceptionCode.TRN_004));
        final BigInteger transactionBlockNumber = receipt.getBlockNumber();

        log.info(">>>>> transactionBlockNumber : {}", transactionBlockNumber);

        BigInteger latestBlockNumber = latestBlockResponse.getBlockNumber();
        final int confirmationNumber = latestBlockNumber.subtract(transactionBlockNumber)
                .intValue();
        log.info(">>>>> transactionHash : {}, confirmationNumber : {}", transactionHash, confirmationNumber);
        return confirmationNumber;
    }

    @Transactional
    public void withdraw(WithdrawDto request) throws Exception {
        final Wallet wallet = walletRepository.findByAddress(request.getFromAddress());
        if (wallet == null) throw new TransactionException(ExceptionCode.TRN_001);

        Credentials credentials = Credentials.create(wallet.getPrivateKey());

        BigInteger nonce = web3jUtils.getWeb3j()
                .ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get()
                .getTransactionCount();
        log.info(">>>>> nonce : {}", nonce);

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                getGasPrice(),
                getGasLimit(),
                request.getToAddress(),
                request.getEtherInWei()
        );
        log.info(">>>>> rawTransaction : {}", rawTransaction);

        String hexValue = signTransaction(credentials, rawTransaction);

        EthSendTransaction transactionResponse = sendRawTransaction(hexValue);
        log.info(">>>>> transactionResponse : {}", transactionResponse);

        if (transactionResponse.hasError()) {
            log.warn(">>>>> sendTransaction error : {}", transactionResponse.getError().getMessage());
            throw new TransactionException(ExceptionCode.TRN_003);
        }

        final String transactionHash = transactionResponse.getTransactionHash();
        log.info(">>>>> transactionHash : {}", transactionHash);

        transactionRepository.save(
                new Transaction(
                        wallet,
                        transactionHash,
                        BigDecimal.valueOf(request.getEtherInWei().longValue()),
                        TransactionType.WITHDRAW
                )
        );
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

    private BigInteger getGasPrice() {
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

    private BigInteger getGasLimit() {
        final BigInteger gasLimit = BigInteger.valueOf(21000);
        log.info(">>>>> gasLimit : {}", gasLimit);
        log.info(">>>>> gasLimitInEther : {}", web3jUtils.convertToEther(gasLimit));
        return gasLimit;
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionEvents(Integer startAfter, Integer endBefore, int size) {

        Specification<Transaction> spec = (root, query, criteriaBuilder) -> {

            Predicate finalPredicate = null;

            if (startAfter != null) {
                finalPredicate = criteriaBuilder.greaterThan(root.get("id"), startAfter);
            }

            if (endBefore != null) {
                Predicate predicateEndBefore = criteriaBuilder.lessThan(root.get("id"), endBefore);
                finalPredicate = finalPredicate == null ? predicateEndBefore
                        : criteriaBuilder.and(finalPredicate, predicateEndBefore);
            }

            if (startAfter != null && endBefore != null && startAfter > endBefore) {
                throw new TransactionException(ExceptionCode.TRN_005);
            }

            return finalPredicate;
        };

        Pageable pageable = PageRequest.of(
                0,
                Math.min(Math.max(size, 10), 100),
                Sort.by(Sort.Direction.DESC,"id")
        );

        return transactionRepository.findAll(spec, pageable);
    }
}

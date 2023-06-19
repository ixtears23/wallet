package junseok.snr.wallet.application.schedule;


import junseok.snr.wallet.application.service.exception.ExceptionCode;
import junseok.snr.wallet.application.service.exception.TransactionException;
import junseok.snr.wallet.domain.model.Transaction;
import junseok.snr.wallet.domain.model.TransactionStatus;
import junseok.snr.wallet.domain.repository.TransactionRepository;
import junseok.snr.wallet.infrastructure.common.Web3jUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Profile("withdraw-schedule")
@Component
public class TransactionWithdrawScheduler {
    private final TransactionRepository transactionRepository;
    private final Web3jUtils web3jUtils;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void updateWithdrawTransactionStatuses() throws Exception {
        log.info(">>>>> updateTransactionStatuses - now : {}", LocalDateTime.now());
        List<Transaction> transactions = transactionRepository.findLatestTransactions(TransactionStatus.CONFIRMED);
        log.info(">>>>> updateTransactionStatuses - transactions : {}", transactions);
        for (Transaction transaction : transactions) {
            saveTransaction(transaction);
        }
    }

    private void saveTransaction(Transaction transaction) throws Exception {
        final int confirmationCount = getConfirmationNumber(transaction.getTransactionHash());
        final Transaction newTransaction = new Transaction(
                transaction.getWallet(),
                transaction.getTransactionHash(),
                transaction.getAmount(),
                transaction.getType()
        );

        try {
            newTransaction.updateStatus(confirmationCount);
        } catch (Exception exception) {
            log.warn(">>>>> updateWithdrawTransactionStatuses error : {}", exception.getMessage());
        }

        if (transaction.isChangedConfirmation(confirmationCount)) {
            transactionRepository.save(newTransaction);
        }
    }


    private int getConfirmationNumber(String transactionHash) throws Exception {
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
}

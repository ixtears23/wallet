package junseok.snr.wallet.application.schedule;


import junseok.snr.wallet.application.service.TransactionService;
import junseok.snr.wallet.domain.model.Transaction;
import junseok.snr.wallet.domain.model.TransactionStatus;
import junseok.snr.wallet.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Profile("withdraw-schedule")
@Component
public class TransactionWithdrawScheduler {
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

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
        final int confirmationCount = transactionService.getConfirmationNumber(transaction.getTransactionHash());
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

}

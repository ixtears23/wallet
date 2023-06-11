package junseok.snr.wallet.application.schedule;

import junseok.snr.wallet.infrastructure.repository.common.Web3jUtils;
import junseok.snr.wallet.domain.model.Transaction;
import junseok.snr.wallet.domain.model.TransactionStatus;
import junseok.snr.wallet.domain.model.TransactionType;
import junseok.snr.wallet.domain.model.Wallet;
import junseok.snr.wallet.infrastructure.repository.TransactionJpaRepository;
import junseok.snr.wallet.infrastructure.repository.WalletJpaRepository;
import junseok.snr.wallet.application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionScheduler {
    private final Web3jUtils web3jUtils;
    private final TransactionService transactionService;
    private final TransactionJpaRepository transactionJpaRepository;
    private final WalletJpaRepository walletJpaRepository;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void updateWithdrawTransactionStatuses() throws Exception {
        log.info(">>>>> updateTransactionStatuses - now : {}", LocalDateTime.now());
        List<Transaction> transactions = transactionJpaRepository.findLatestTransactions(TransactionStatus.CONFIRMED);
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
            transactionJpaRepository.save(newTransaction);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void processDeposit()  {
        log.info(">>>>> processDeposit");
        final List<Wallet> walletList = walletJpaRepository.findAll();
        walletList.forEach(this::monitoringDeposit);
    }

    private void monitoringDeposit(Wallet wallet) {
        web3jUtils.getWeb3j()
                .transactionFlowable()
                .subscribe(transaction -> {
                    if (transaction.getTo() != null
                            && transaction.getTo()
                            .equals(wallet.getAddress())) {
                        log.info(">>>>> transaction.getTo() : {}", transaction.getTo());
                        log.info(">>>>> monitoringDeposit - transaction_hash : {}", transaction.getHash());

                        BigInteger weiValue = transaction.getValue();
                        log.info(">>>>> weiValue : {}", weiValue);

                        final Transaction findTransaction = transactionJpaRepository.findFirstByTransactionHashAndType(transaction.getHash(), TransactionType.DEPOSIT);

                        if (findTransaction == null) {
                            transactionJpaRepository.save(
                                    new Transaction(
                                            wallet,
                                            transaction.getHash(),
                                            BigDecimal.valueOf(transaction.getValue().longValue()),
                                            TransactionType.DEPOSIT
                                    )
                            );
                        }
                    }
                });
    }

}
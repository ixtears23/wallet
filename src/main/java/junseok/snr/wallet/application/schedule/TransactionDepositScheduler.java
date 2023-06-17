package junseok.snr.wallet.application.schedule;


import junseok.snr.wallet.domain.model.Transaction;
import junseok.snr.wallet.domain.model.TransactionType;
import junseok.snr.wallet.domain.model.Wallet;
import junseok.snr.wallet.domain.repository.TransactionRepository;
import junseok.snr.wallet.domain.repository.WalletRepository;
import junseok.snr.wallet.infrastructure.common.Web3jUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Profile("deposit-schedule")
@Component
public class TransactionDepositScheduler {
    private final Web3jUtils web3jUtils;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void processDeposit()  {
        log.info(">>>>> processDeposit");
        final List<Wallet> walletList = walletRepository.findAll();
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

                        final Transaction findTransaction = transactionRepository.findFirstByTransactionHashAndType(transaction.getHash(), TransactionType.DEPOSIT);

                        if (findTransaction == null) {
                            transactionRepository.save(
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

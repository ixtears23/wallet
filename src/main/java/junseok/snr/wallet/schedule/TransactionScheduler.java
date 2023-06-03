package junseok.snr.wallet.schedule;

import junseok.snr.wallet.Web3jUtils;
import junseok.snr.wallet.api.domain.Transaction;
import junseok.snr.wallet.api.domain.TransactionStatus;
import junseok.snr.wallet.api.domain.Wallet;
import junseok.snr.wallet.api.repository.TransactionRepository;
import junseok.snr.wallet.api.repository.WalletRepository;
import junseok.snr.wallet.api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionScheduler {
    private final Web3jUtils web3jUtils;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void updateWithdrawTransactionStatuses() throws Exception {
        log.debug(">>>>> updateTransactionStatuses - now : {}", LocalDateTime.now());
        List<Transaction> transactions = transactionRepository.findByStatusNot(TransactionStatus.CONFIRMED);
        log.debug(">>>>> updateTransactionStatuses - transactions : {}", transactions);
        for (Transaction transaction : transactions) {
            final int confirmationCount = transactionService.getConfirmationNumber(transaction.getTransactionHash());
            transaction.updateStatus(confirmationCount);
            transactionRepository.save(transaction);
        }
    }

    // TODO 블록체인 상태 확인 메서드가 존재해야 함
    @Scheduled(fixedRate = 5000)
    public void checkTransactionStatus() throws Exception {
        List<Transaction> transactions = transactionRepository.findByStatusNot(TransactionStatus.CONFIRMED);
        for (Transaction transaction : transactions) {

            EthGetTransactionReceipt receipt = web3jUtils.getWeb3j()
                    .ethGetTransactionReceipt(transaction.getTransactionHash())
                    .send();
        }
    }

}

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

import java.io.IOException;
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
        log.info(">>>>> updateTransactionStatuses - now : {}", LocalDateTime.now());
        List<Transaction> transactions = transactionRepository.findByStatusNot(TransactionStatus.CONFIRMED);
        log.info(">>>>> updateTransactionStatuses - transactions : {}", transactions);
        for (Transaction transaction : transactions) {
            final int confirmationCount = transactionService.getConfirmationNumber(transaction.getTransactionHash());
            transaction.updateStatus(confirmationCount);
            transactionRepository.save(transaction);
        }
    }

    // TODO 블록체인 상태 확인 메서드가 존재해야 함
    @Scheduled(fixedRate = 5000)
    public void checkTransactionStatus() {
        List<Transaction> transactions = transactionRepository.findAllByStatusNot(TransactionStatus.CONFIRMED);
        for (Transaction transaction : transactions) {
            EthGetTransactionReceipt receipt = null;
            try {
                receipt = web3jUtils.getWeb3j()
                        .ethGetTransactionReceipt(transaction.getTransactionHash())
                        .send();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (receipt != null && receipt.getTransactionReceipt().isPresent()) {
                int confirmations = getConfirmationNumber(receipt.getTransactionReceipt().get().getBlockNumber());

                if (confirmations >= 12) {
                    transaction.setStatus(TransactionStatus.CONFIRMED);
                    Wallet wallet = walletRepository.findByAddress(transaction.getAddress());
                    if (wallet != null) {
                        wallet.setBalance(wallet.getBalance().add(transaction.getAmount())); // 입금이면
                        // wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount())); // 출금이면
                        walletRepository.save(wallet);
                    }

                } else if (confirmations > 0) {
                    transaction.setStatus(TransactionStatus.MINED);
                }
                transaction.setConfirmations(confirmations);
                transactionRepository.save(transaction);
            }
        }
    }

}

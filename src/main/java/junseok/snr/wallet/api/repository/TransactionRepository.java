package junseok.snr.wallet.api.repository;

import junseok.snr.wallet.api.domain.Transaction;
import junseok.snr.wallet.api.domain.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByStatusNot(TransactionStatus status);
    List<Transaction> findTop10ByTransactionHashGreaterThanOrderByTransactionHashAsc(String transactionHash);
    List<Transaction> findTop10ByTransactionHashLessThanOrderByTransactionHashDesc(String transactionHash);
    List<Transaction> findTop10ByOrderByTransactionHashDesc();
}

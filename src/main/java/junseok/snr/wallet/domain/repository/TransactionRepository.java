package junseok.snr.wallet.domain.repository;

import junseok.snr.wallet.domain.model.Transaction;
import junseok.snr.wallet.domain.model.TransactionStatus;
import junseok.snr.wallet.domain.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TransactionRepository {
    Transaction findFirstByTransactionHashAndType(String transactionHash, TransactionType transactionType);
    List<Transaction> findLatestTransactions(TransactionStatus status);
    Transaction save(Transaction transaction);
    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable);
}

package junseok.snr.wallet.domain.repository;

import junseok.snr.wallet.domain.model.Transaction;
import junseok.snr.wallet.domain.model.TransactionStatus;
import junseok.snr.wallet.domain.model.TransactionType;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository {
    Transaction findFirstByTransactionHashAndType(String transactionHash, TransactionType transactionType);
    List<Transaction> findLatestTransactions(@Param("status") TransactionStatus status);
}

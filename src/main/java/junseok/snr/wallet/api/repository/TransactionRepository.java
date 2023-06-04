package junseok.snr.wallet.api.repository;

import junseok.snr.wallet.api.domain.Transaction;
import junseok.snr.wallet.api.domain.TransactionStatus;
import junseok.snr.wallet.api.domain.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
     Transaction findFirstByTransactionHashAndType(String transactionHash, TransactionType transactionType);

    @Query(value = """
                SELECT t.*
                  FROM transaction t
                  JOIN (SELECT ti.transaction_hash,
                               ti.type,
                               MAX(ti.confirmation_count) AS max_confirmation,
                               MAX(ti.id) AS max_id
                          FROM transaction ti
                      GROUP BY ti.transaction_hash,
                               ti.type
                        HAVING MAX(ti.confirmation_count) IS NULL
                            OR MAX(ti.confirmation_count) < 12) tm
                    ON t.id = tm.max_id
                   AND t.transaction_hash = tm.transaction_hash""",
            nativeQuery = true)
    List<Transaction> findLatestTransactions(@Param("status") TransactionStatus status);

    List<Transaction> findTop10ByTransactionHashGreaterThanOrderByTransactionHashAsc(String transactionHash);
    List<Transaction> findTop10ByTransactionHashLessThanOrderByTransactionHashDesc(String transactionHash);
    List<Transaction> findTop10ByOrderByTransactionHashDesc();
}

package junseok.snr.wallet.infrastructure.repository;

import junseok.snr.wallet.domain.model.Transaction;
import junseok.snr.wallet.domain.model.TransactionStatus;
import junseok.snr.wallet.domain.repository.TransactionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction>, TransactionRepository {
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
    @Override
    List<Transaction> findLatestTransactions(@Param("status") TransactionStatus status);
}

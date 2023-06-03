package junseok.snr.wallet.repository;

import junseok.snr.wallet.domain.Transaction;
import junseok.snr.wallet.domain.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByStatusNot(TransactionStatus status);
}

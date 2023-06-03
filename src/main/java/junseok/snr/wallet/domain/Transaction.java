package junseok.snr.wallet.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Transaction extends BaseEntity {
    @Transient
    private static final int CONFIRM_COUNT = 12;

    @Id
    @GeneratedValue
    private Long id;
    private String transactionHash;
    private int confirmationCount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @ManyToOne
    private Wallet wallet;

    public Transaction(String transactionHash) {
        this.transactionHash = transactionHash;
        this.status = TransactionStatus.PENDING;
    }

    public void updateStatus(int confirmationCount) {
        this.confirmationCount = confirmationCount;
        if (isMined(confirmationCount)) status = TransactionStatus.MINED;
        if (isConfirmed(confirmationCount)) status = TransactionStatus.CONFIRMED;
    }

    public boolean isConfirmed(int confirmationCount) {
        return CONFIRM_COUNT <= confirmationCount;
    }

    public boolean isMined(int confirmationCount) {
        return CONFIRM_COUNT > confirmationCount;
    }
}

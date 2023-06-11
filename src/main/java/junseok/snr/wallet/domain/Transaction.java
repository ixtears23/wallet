package junseok.snr.wallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import junseok.snr.wallet.application.service.ExceptionCode;
import junseok.snr.wallet.application.service.TransactionException;
import junseok.snr.wallet.application.service.WalletException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Transaction extends BaseEntity {
    private static final int CONFIRM_COUNT = 12;

    @Id
    @GeneratedValue
    private Long id;
    private String transactionHash;
    private Integer confirmationCount;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public Transaction(Wallet wallet, String transactionHash, BigDecimal amount, TransactionType type) {
        if (wallet == null) throw new TransactionException(ExceptionCode.TRN_001);

        if (TransactionType.WITHDRAW.equals(type)
                && wallet.isWithdrawalImpossible(amount)) {
            throw new WalletException(ExceptionCode.TRN_002);
        }

        this.wallet = wallet;
        this.transactionHash = transactionHash;
        this.amount = amount;
        this.type = type;
        initializeStatus();
    }

    private void initializeStatus() {
        this.status = TransactionStatus.PENDING;
    }

    public boolean isChangedConfirmation(int confirmationCount) {
        return this.confirmationCount == null
                || this.confirmationCount < confirmationCount;
    }

    public void updateStatus(int confirmationCount) {
        this.confirmationCount = confirmationCount;
        if (isMined(confirmationCount)) mine();
        if (isConfirmed(confirmationCount)) confirm();
    }

    private boolean isMined(int confirmationCount) {
        return CONFIRM_COUNT > confirmationCount;
    }

    private void mine() {
        this.status = TransactionStatus.MINED;
    }

    private boolean isConfirmed(int confirmationCount) {
        return CONFIRM_COUNT <= confirmationCount;
    }

    private void confirm() {
        this.status = TransactionStatus.CONFIRMED;
        if (TransactionType.DEPOSIT.equals(type)) wallet.deposit(amount);
        if (TransactionType.WITHDRAW.equals(type)) wallet.withdraw(amount);
    }

}

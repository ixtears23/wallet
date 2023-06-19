package junseok.snr.wallet.domain.model;


import jakarta.persistence.*;
import junseok.snr.wallet.infrastructure.common.AESEncryptionService;
import junseok.snr.wallet.application.service.exception.ExceptionCode;
import junseok.snr.wallet.application.service.exception.WalletException;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
public class Wallet extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String address;
    private String password;
    private String privateKey;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private WalletType walletType;
    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions = new ArrayList<>();

    public Wallet(String address, String password, String privateKey, WalletType walletType) {
        this.address = address;
        this.password = password;
        this.privateKey = AESEncryptionService.encrypt(privateKey, this.password);
        this.balance = BigDecimal.ZERO;
        this.walletType = walletType;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(Transaction transaction) {
        if (isWithdrawalImpossible(transaction.getAmount())) {
            throw new WalletException(ExceptionCode.WAL_002);
        }
        balance = balance.subtract(transaction.getAmount());
        transactions.remove(transaction);
    }

    public boolean isWithdrawalImpossible(BigDecimal amount) {

        BigDecimal totalTransactionAmount = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            totalTransactionAmount = totalTransactionAmount.add(transaction.getAmount());
        }

        final BigDecimal subtractBalance = balance.subtract(totalTransactionAmount);

        return subtractBalance.compareTo(amount) < 0;
    }

    public String getPrivateKey() {
        return AESEncryptionService.decrypt(this.privateKey, this.password);
    }


}

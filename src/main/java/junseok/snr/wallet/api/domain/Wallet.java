package junseok.snr.wallet.api.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


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

    public Wallet(String address, String password, String privateKey, WalletType walletType) {
        this.address = address;
        this.password = password;
        this.privateKey = privateKey;
        this.balance = BigDecimal.ZERO;
        this.walletType = walletType;
    }
}

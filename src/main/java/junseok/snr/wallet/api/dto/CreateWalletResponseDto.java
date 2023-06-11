package junseok.snr.wallet.api.dto;

import junseok.snr.wallet.domain.WalletType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateWalletResponseDto {

    private Long id;
    private String address;
    private BigDecimal balance;
    private WalletType walletType;
}

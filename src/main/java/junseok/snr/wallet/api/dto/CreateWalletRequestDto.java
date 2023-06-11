package junseok.snr.wallet.api.dto;

import jakarta.validation.constraints.NotNull;
import junseok.snr.wallet.domain.WalletType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateWalletRequestDto {

    @NotNull
    private String password;
    @NotNull
    private WalletType walletType;
}

package junseok.snr.wallet.api.controller.dto;

import jakarta.validation.constraints.NotNull;
import junseok.snr.wallet.api.domain.WalletType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateWalletDto {

    @NotNull
    private String password;
    @NotNull
    private WalletType walletType;
}

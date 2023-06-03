package junseok.snr.wallet.api.controller.dto;

import jakarta.validation.constraints.NotNull;
import junseok.snr.wallet.api.domain.WalletType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateWalletDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Request {
        @NotNull
        private String password;
        @NotNull
        private WalletType walletType;
    }

}

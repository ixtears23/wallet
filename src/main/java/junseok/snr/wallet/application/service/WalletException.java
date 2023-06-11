package junseok.snr.wallet.application.service;

import lombok.Getter;

@Getter
public class WalletException extends RuntimeException {
    private final ExceptionCode code;
    public WalletException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
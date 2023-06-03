package junseok.snr.wallet.service;

import lombok.Getter;

@Getter
public class BalanceException extends RuntimeException {
    private final ExceptionCode code;
    public BalanceException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }
}

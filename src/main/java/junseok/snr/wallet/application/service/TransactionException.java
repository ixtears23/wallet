package junseok.snr.wallet.application.service;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {
    private final ExceptionCode code;
    public TransactionException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }
}

package junseok.snr.wallet;

import junseok.snr.wallet.api.service.TransactionException;
import junseok.snr.wallet.api.service.WalletException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WalletException.class)
    public ExceptionResponse handleRuntimeException(WalletException exception) {
        return ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(TransactionException.class)
    public ExceptionResponse handleRuntimeException(TransactionException exception) {
        return ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

}

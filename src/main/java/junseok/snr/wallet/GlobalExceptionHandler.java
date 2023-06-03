package junseok.snr.wallet;

import junseok.snr.wallet.service.BalanceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BalanceException.class)
    public ExceptionResponse handleRuntimeException(BalanceException exception) {
        return ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }
}

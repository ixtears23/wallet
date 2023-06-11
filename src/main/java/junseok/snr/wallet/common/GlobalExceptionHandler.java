package junseok.snr.wallet.common;

import junseok.snr.wallet.application.service.TransactionException;
import junseok.snr.wallet.application.service.WalletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(exception.getMessage())
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WalletException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(WalletException exception) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(TransactionException exception) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

}

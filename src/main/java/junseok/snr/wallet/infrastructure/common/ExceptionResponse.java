package junseok.snr.wallet.infrastructure.common;

import junseok.snr.wallet.application.service.exception.ExceptionCode;
import lombok.Builder;

@Builder
public class ExceptionResponse {
    private final ExceptionCode code;
    private final String message;
}

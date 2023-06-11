package junseok.snr.wallet.infrastructure.repository.common;

import junseok.snr.wallet.application.service.ExceptionCode;
import lombok.Builder;

@Builder
public class ExceptionResponse {
    private final ExceptionCode code;
    private final String message;
}

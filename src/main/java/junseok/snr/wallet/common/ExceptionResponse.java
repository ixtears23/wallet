package junseok.snr.wallet.common;

import junseok.snr.wallet.application.service.ExceptionCode;
import lombok.Builder;

@Builder
public class ExceptionResponse {
    private final ExceptionCode code;
    private final String message;
}

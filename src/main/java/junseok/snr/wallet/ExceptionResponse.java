package junseok.snr.wallet;

import junseok.snr.wallet.api.service.ExceptionCode;
import lombok.Builder;

@Builder
public class ExceptionResponse {
    private final ExceptionCode code;
    private final String message;
}

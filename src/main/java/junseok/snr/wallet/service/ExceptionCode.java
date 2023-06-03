package junseok.snr.wallet.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    BLA_001("주소 없이 잔액조회를 할 수 없습니다.");

    private final String message;
}

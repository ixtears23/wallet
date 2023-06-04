package junseok.snr.wallet.api.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    WAL_001("주소 없이 잔액조회를 할 수 없습니다."),
    WAL_002("지갑의 잔액이 부족해서 출금할 수 없습니다."),
    WAL_003("privateKey 암호화 오류"),
    WAL_004("privateKey 복호화 오류"),

    TRN_001("트랜잭션 생성 시 지갑은 null일 수 없습니다."),
    TRN_002("지갑의 잔액이 부족해서 출금할 수 없습니다."),
    TRN_003("트랜잭션 전송 시 에러 발생"),
    TRN_004("트랜잭션 영수증이 아직 생성되지 않았습니다."),
    TRN_005("startAfter 는 endBefore 보다 클 수 없습니다.");

    private final String message;
}

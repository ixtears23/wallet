package junseok.snr.wallet.api.dto;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WithdrawDto {
    private String fromAddress;
    private String toAddress;
    private BigInteger etherInWei;
}

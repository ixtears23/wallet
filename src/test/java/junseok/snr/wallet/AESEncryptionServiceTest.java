package junseok.snr.wallet;

import junseok.snr.wallet.common.AESEncryptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AESEncryptionServiceTest {

    @DisplayName("암호화된 privateKey 를 암호화할 때 사용한 secretKey로 복호화하면 암호화하기 전의 privateKey와 일치해야 한다.")
    @Test
    void encryptAndDecryptTest() {
        final String secret = "abcdefg89";
        final String privateKey = "aaaaaaa";

        final String encryptedPrivateKey = AESEncryptionService.encrypt(privateKey, secret);
        final String decryptedPrivateKey = AESEncryptionService.decrypt(encryptedPrivateKey, secret);

        assertThat(decryptedPrivateKey).isEqualTo(privateKey);
    }
}
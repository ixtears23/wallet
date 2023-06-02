package junseok.snr.heachi02;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletJson {

    private String address;
    private String id;
    private int version;
    private Crypto crypto;

    // getters and setters ...
    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Crypto {
        private String cipher;
        private String ciphertext;
        private Cipherparams cipherparams;
        private String kdf;
        private Kdfparams kdfparams;
        private String mac;

        // getters and setters ...
        @Getter @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Cipherparams {
            private String iv;

            // getters and setters ...
        }
        @Getter @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Kdfparams {
            private int dklen;
            private int n;
            private int p;
            private int r;
            private String salt;

            // getters and setters ...
        }
    }
}
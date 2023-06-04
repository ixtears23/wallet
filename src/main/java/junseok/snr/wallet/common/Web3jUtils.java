package junseok.snr.wallet.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
public class Web3jUtils {
    @Getter
    private final Web3j web3j;
    public Web3jUtils(@Value("${node.endpoint.ethereum}") String etherNodeEndpoint) {
        this.web3j = Web3j.build(new HttpService(etherNodeEndpoint));
    }

    public BigDecimal convertToEther(BigInteger balanceInWei) {
        return Convert.fromWei(new BigDecimal(balanceInWei), Convert.Unit.ETHER)
                .setScale(5, RoundingMode.HALF_UP);
    }

    public String getPrivateKey(String jsonFilePatch) throws Exception {
        final Credentials credentials = org.web3j.crypto.WalletUtils.loadJsonCredentials("1234qwer", readJsonFileToString(jsonFilePatch));
        String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        log.info(">>>>> privateKey : {}", privateKey);
        return privateKey;
    }

    public String readJsonFileToString(String jsonFilePath) throws IOException {
        return new String(Files.readAllBytes(Path.of(jsonFilePath)));
    }

}

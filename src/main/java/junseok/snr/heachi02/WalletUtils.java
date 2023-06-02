package junseok.snr.heachi02;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class WalletUtils {
    private static final String ETH_NODE_ENDPOINT = "https://tn.henesis.io/ethereum/goerli?clientId=815fcd01324b8f75818a755a72557750";
    @Getter
    private static final Web3j web3j = Web3j.build(new HttpService(ETH_NODE_ENDPOINT)); // 이더리움 노드 엔드포인트

    public static BigDecimal convertToEther(BigInteger balanceInWei) {
        return Convert.fromWei(new BigDecimal(balanceInWei), Convert.Unit.ETHER)
                .setScale(5, RoundingMode.HALF_UP);
    }

    public static String getPrivateKey(String jsonFilePatch) throws Exception {
        final Credentials credentials = org.web3j.crypto.WalletUtils.loadJsonCredentials("1234qwer", readJsonFileToString(jsonFilePatch));
        String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        log.info(">>>>> privateKey : {}", privateKey);
        return privateKey;
    }

    public static String readJsonFileToString(String jsonFilePath) throws IOException {
        return new String(Files.readAllBytes(Path.of(jsonFilePath)));
    }

}

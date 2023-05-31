package junseok.snr.heachi01.domain;

import lombok.Getter;
import lombok.ToString;
import org.web3j.crypto.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Getter
@ToString
public class EthereumWallet {
    private final String publicKey;
    private final String privateKey;
    private final String address;

    public EthereumWallet(final String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        WalletFile wallet = Wallet.createStandard(password, ecKeyPair);
        Credentials credentials = Credentials.create(ecKeyPair);
        privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        publicKey = credentials.getEcKeyPair().getPublicKey().toString(16);
        address = wallet.getAddress();
    }

}

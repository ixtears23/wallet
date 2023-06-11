package junseok.snr.wallet.infrastructure.repository.common;

import junseok.snr.wallet.application.service.ExceptionCode;
import junseok.snr.wallet.application.service.WalletException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AESEncryptionService {
    private static final int IV_SIZE = 12;

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            Key secretKey = new SecretKeySpec(hashPasswordTo256BitKey(secret), "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] iv = new byte[IV_SIZE];
            new SecureRandom().nextBytes(iv);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
            byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
            byte[] cipherWithIv = ByteBuffer.allocate(iv.length + encrypted.length).put(iv).put(encrypted).array();
            return Base64.getEncoder().encodeToString(cipherWithIv);
        } catch (Exception e) {
            log.warn("encrypt error - message : {}", e.getMessage());
            throw new WalletException(ExceptionCode.WAL_003);
        }
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            Key secretKey = new SecretKeySpec(hashPasswordTo256BitKey(secret), "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] decode = Base64.getDecoder().decode(strToDecrypt);
            ByteBuffer bb = ByteBuffer.wrap(decode);
            byte[] iv = new byte[IV_SIZE];
            bb.get(iv);
            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("decrypt error - message : {}", e.getMessage());
            throw new WalletException(ExceptionCode.WAL_004);
        }
    }

    private static byte[] hashPasswordTo256BitKey(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
    }
}

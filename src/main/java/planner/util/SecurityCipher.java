package planner.util;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static javax.crypto.Cipher.getInstance;
import static planner.config.template.SecurityCipherConfig.DIGEST_ALGORITHM;
import static planner.config.template.SecurityCipherConfig.DIGEST_ARRAY_SIZE;
import static planner.config.template.SecurityCipherConfig.ENCRYPT_STANDARD;
import static planner.config.template.SecurityCipherConfig.TRANSFORMATION;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityCipher {
    private static String cipherKey;
    private static SecretKeySpec secretKey;

    @Value("${security.cipher.secret-key}")
    public void setNameStatic(String cipherRaw) {
        cipherKey = cipherRaw;
    }

    @SneakyThrows
    public static void setKey() {
        MessageDigest sha;
        byte[] key = cipherKey.getBytes(StandardCharsets.UTF_8);
        sha = MessageDigest.getInstance(DIGEST_ALGORITHM.value());
        key = sha.digest(key);
        key = Arrays.copyOf(key, Integer.parseInt(DIGEST_ARRAY_SIZE.value()));
        secretKey = new SecretKeySpec(key, ENCRYPT_STANDARD.value());
    }

    @SneakyThrows
    public static String encrypt(String strToEncrypt) {
        if (strToEncrypt != null && !strToEncrypt.isEmpty()) {
            setKey();
            Cipher cipher = getInstance(TRANSFORMATION.value());
            cipher.init(ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        return null;
    }

    @SneakyThrows
    public static String decrypt(String strToDecrypt) {
        if (strToDecrypt != null && !strToDecrypt.isEmpty()) {
            setKey();
            Cipher cipher = getInstance(TRANSFORMATION.value());
            cipher.init(DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        return null;
    }
}

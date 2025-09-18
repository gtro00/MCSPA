package com.gtro;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AesUtil {

    private static final String ALGORITHM = "AES";

    public static String encrypt(String data, String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        String ivBase64 = Base64.getEncoder().encodeToString(iv);
        String cipherBase64 = Base64.getEncoder().encodeToString(encrypted);

        return ivBase64 + "##" + cipherBase64;
    }

}

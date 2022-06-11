package com.example.nosql.authentication;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Encryption {
    private static final String ALGORITHM_NAME = "AES";
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void prepareSecreteKey(String myKey) {
        MessageDigest sha_256;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha_256 = MessageDigest.getInstance("SHA-256");
            key = sha_256.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception err) {
            System.out.println("Sorry :( an Error happened during encryption. ==> " + err.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception err) {
            System.out.println("Sorry :( an Error happened during decryption. ==> " + err.toString());
        }
        return null;
    }
}

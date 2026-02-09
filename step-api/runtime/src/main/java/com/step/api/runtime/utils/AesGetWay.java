package com.step.api.runtime.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class AesGetWay {

    private static final Logger LOGGER = LoggerFactory.getLogger(AesGetWay.class);
    private static final String KEY_ALGORITHM_AES = "AES";
    private static final String CIPHER_ALGORITHM_AES = "AES/ECB/PKCS5Padding";
    private static final String AES_CBC_PKCS7 = "AES/CBC/PKCS7Padding";
    public static final String ENCRY_PT_KEY = "1234!@#$";
    public static byte[] encode(String content, String encryptKey) {
        int len = encryptKey.length();
        if (len > 16) {
            encryptKey = encryptKey.substring(0, 16);
        } else if (len < 16) {
            encryptKey = encryptKey + getNumOfZero(16 - len);
        }
        try {
            vailKeyLength(encryptKey);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_AES);
            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM_AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 加密后转换成Base64字符串
     */
    public static String encodeToBase64(String content, String encryptKey) {
        int len = encryptKey.length();
        if (len > 16) {
            encryptKey = encryptKey.substring(0, 16);
        } else if (len < 16) {
            encryptKey = encryptKey + getNumOfZero(16 - len);
        }
        byte[] bytes = encode(content, encryptKey);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Aes解密
     */
    public static String decode(byte[] encryptBytes, byte[] decryptKey, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS7);
            SecretKeySpec secretKeySpec = new SecretKeySpec(decryptKey, KEY_ALGORITHM_AES);
            if (iv!=null) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            }
            byte[] bytes = cipher.doFinal(encryptBytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }


    public static String decode(byte[] encryptBytes, String decryptKey) {
        int len = decryptKey.length();
        if (len > 16) {
            decryptKey = decryptKey.substring(0, 16);
        } else if (len < 16) {
            decryptKey = decryptKey + getNumOfZero(16 - len);
        }
        try {
            vailKeyLength(decryptKey);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_AES);
            SecretKeySpec secretKeySpec = new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM_AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] bytes = cipher.doFinal(encryptBytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public static String decode(String encryptString, String decryptKey, String iv) {
        byte[] encryptStringBytes = Base64.getDecoder().decode(encryptString);
        byte[] decryptKeyBytes = Base64.getDecoder().decode(decryptKey);
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        return decode(encryptStringBytes, decryptKeyBytes, ivBytes);
    }

    public static String decode(String encryptString, String decryptKey) {
        return decode(Base64.getDecoder().decode(encryptString), decryptKey);
    }

    /**
     * 校验AES的秘钥必须为16位长度  否则抛出runtime异常
     */
    private static void vailKeyLength(String key) {
        if (key.length() != 16) {
            throw new RuntimeException("key length must be 16");
        }
    }

    /**
     * 补零
     */
    private static String getNumOfZero(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append("0");
        }
        return sb.toString();
    }
}

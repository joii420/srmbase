package com.step.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public final class Hash {

    private static final Logger log = LoggerFactory.getLogger(Hash.class);
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Md5算法
     */
    public static String md5(String message) {
        return md5(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Md5算法
     */
    public static String md5(byte[] message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(message);
            byte[] digest = messageDigest.digest();
            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
            }
            return result.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Sha1算法
     */
    public static String sha1(String message) {
        return sha1(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Sha1算法
     */
    public static String sha1(byte[] message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(message);
            byte[] digest = messageDigest.digest();
            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(HEX_DIGITS[(b >> 4) & 0x0f]).append(HEX_DIGITS[b & 0x0f]);
            }
            return result.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Sha256算法
     */
    public static String sha256(String message) {
        return sha256(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Sha256算法
     */
    public static String sha256(byte[] message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message);
            byte[] digest = messageDigest.digest();
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    // 把字节数组转换成十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        // 方案1：手写循环（最快且不依赖外部库）
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Character.forDigit((b >>> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }

}

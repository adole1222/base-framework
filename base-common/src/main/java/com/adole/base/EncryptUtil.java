package com.adole.base;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * 加密工具类
 */
public class EncryptUtil {

    private final static String paramEncrpt = "ADOLE";

    private final static String passwordPwdEncrpt = "ADOLEPWD";

    public static String encode(String input, String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(password);
        return encryptor.encrypt(input);
    }

    public static String encode(String input) {
        return encode(input, paramEncrpt);
    }

    public static String encodePwd(String input) {
        return encode(input, passwordPwdEncrpt);
    }

    public static String decode(String input, String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(password);
        return encryptor.decrypt(input);
    }

    public static String decodePwd(String input) {
        return decode(input, passwordPwdEncrpt);
    }

    public static String decode(String input) {
        return decode(input, paramEncrpt);
    }


    public static void main(String[] args) {
        String root = encodePwd("123456");
        System.out.println(root);
    }
}

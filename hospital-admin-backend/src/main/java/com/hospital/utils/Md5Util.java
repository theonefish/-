package com.hospital.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class Md5Util {

    private static final String SALT = "hospital_salt_2024";

    public static String encrypt(String password) {
        String salted = password + SALT;
        return DigestUtils.md5DigestAsHex(salted.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return encrypt(rawPassword).equals(encodedPassword);
    }
}

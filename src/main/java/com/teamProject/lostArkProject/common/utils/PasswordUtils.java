package com.teamProject.lostArkProject.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public static String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

}
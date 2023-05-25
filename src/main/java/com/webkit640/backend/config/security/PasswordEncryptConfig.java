package com.webkit640.backend.config.security;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
public class PasswordEncryptConfig {
    public String makeMD5(String password) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            md5 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            md5 = null;
        }
        return md5;
    }
}

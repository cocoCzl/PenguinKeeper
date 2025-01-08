package com.dollar.penguin.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CipherUtil {

    private static final Cipher CIPHER = new Cipher(
            new byte[]{0x72, 0x34, 0x78, 0x38, 0x68, 0x6b, 0x4a, 0x42, 0x75, 0x59, 0x75, 0x52, 0x62,
                    0x75, 0x6a, 0x6e});

    public static String encode(String content) {
        byte[] encryptBytes = CIPHER.encrypt(content.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(encryptBytes), StandardCharsets.UTF_8);
    }

    public static String decode(String content) {
        byte[] originBytes = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
        byte[] decryptBytes = CIPHER.decrypt(originBytes);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            stringBuilder.append("0x");
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}

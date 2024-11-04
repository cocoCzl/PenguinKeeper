package com.dollar.penguin.util;

import java.nio.charset.StandardCharsets;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;

public class TEAUtil {

    private static final TEA tea = new TEA(
            new byte[]{0x72, 0x34, 0x78, 0x38, 0x68, 0x6b, 0x4a, 0x42, 0x75, 0x59, 0x75, 0x52, 0x62,
                    0x75, 0x6a, 0x6e});

    public static String encode(String content) {
        byte[] encryptBytes = tea.encrypt(content.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.encode(encryptBytes), StandardCharsets.UTF_8);
    }

    public static String decode(String content) {
        byte[] originBytes = Base64.decode(content.getBytes(StandardCharsets.UTF_8));
        byte[] decryptBytes = tea.decrypt(originBytes);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        String text = "develop";
        String encrypt = encode((text));
        String decrypt = decode(encrypt);
        System.out.println("encrypt:" + encrypt);
        System.out.println("decrypt:" + decrypt);
        System.out.println("origin :" + text);
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

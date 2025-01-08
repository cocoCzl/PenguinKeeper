package com.dollar.penguin.util;

import org.junit.jupiter.api.Test;

public class CipherTest {

    @Test
    public void cipherTest() {
        String text = "develop123***(02de";
        String encrypt = CipherUtil.encode((text));
        String decrypt = CipherUtil.decode(encrypt);
        System.out.println("encrypt:" + encrypt);
        System.out.println("decrypt:" + decrypt);
        System.out.println("origin :" + text);
    }
}

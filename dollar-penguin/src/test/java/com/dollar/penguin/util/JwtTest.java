package com.dollar.penguin.util;

import com.auth0.jwt.interfaces.Claim;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JwtTest {

    @Test
    public void jwtTest() {
        Map<String, Object> data = new HashMap<>();
        data.put("user", "Asuna");
        data.put("age", 18);
        data.put("addr", "shanghai");
        String title = "user";
        String token = JwtUtil.doGen(title, data);
        System.out.println(token);
        Claim claim = JwtUtil.doParse(token, title);
        System.out.println(claim);
    }
}

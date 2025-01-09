package com.dollar.penguin.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    public static final String KEY = "dollar-penguin";

    /**
     * 生成令牌
     * */
    public static String doGen(String title, Map<String, Object> data) {
        return JWT.create().withClaim(title, data)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)).sign(
                        Algorithm.HMAC256(KEY));
    }

    /**
     * 解析令牌
     */
    public static Claim doParse(String token, String title) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(KEY)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> claimMap = decodedJWT.getClaims();
        return claimMap.get(title);
    }

}

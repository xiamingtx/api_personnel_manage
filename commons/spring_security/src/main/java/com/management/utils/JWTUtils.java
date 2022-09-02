package com.management.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.management.config.SecurityConfiguration;

import java.util.Date;

/**
 * @author 夏明
 * @version 1.0
 */
public class JWTUtils {
    public static String createJWT(String subject, Long duration) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(Algorithm.HMAC512(SecurityConfiguration.SECRET.getBytes()));
    }

    public static String getSubject(String token) {
        return JWT.require(Algorithm.HMAC512(SecurityConfiguration.SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }
}

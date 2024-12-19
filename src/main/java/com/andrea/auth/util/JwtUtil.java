package com.andrea.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "dt7462v3898nf72312r4fwrgv21e3dqfewcceh3w1ych2ewyb432b343786bv8734bv346bv130478b37v";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();
    private static final long EXPIRATION_TIME = 3600000; // 1 ora

    public static String generateToken(int id_account) {
        String subjectToken = String.valueOf(id_account);
        return JWT.create()
                .withSubject(subjectToken)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);
    }

    public static String validateToken(String token) throws JWTVerificationException {
        DecodedJWT jwt = VERIFIER.verify(token);
        return jwt.getSubject();
    }
}

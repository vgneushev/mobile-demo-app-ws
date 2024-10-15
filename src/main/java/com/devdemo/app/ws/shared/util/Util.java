package com.devdemo.app.ws.shared.util;

import com.devdemo.app.ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.qala.datagen.RandomShortApi;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class Util {
    public static boolean hasTokenExpired(@NonNull final String token) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret()).build()
                    .parseClaimsJws(token)
                    .getBody();

            Date tokenExpirationDate = claims.getExpiration();
            Date today = new Date();
            return tokenExpirationDate.before(today);
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired");
            return Boolean.FALSE;
        }
    }

    public static String generateTokenForUserId(@NonNull final String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
    }

    public static String generatePasswordResetToken(@NonNull final String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
    }

    public static String generateUserId() {
        return RandomShortApi.alphanumeric(Constant.USER_ID_LENGTH);
    }
}

package com.devdemo.app.ws.shared.util;

import com.devdemo.app.ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;

import java.util.Date;

public class Util {
    public static boolean hasTokenExpired(@NonNull final String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.getTokenSecret()).build()
                .parseClaimsJws(token).getBody();

        Date tokenExpirationDate = claims.getExpiration();
        Date today = new Date();

        return tokenExpirationDate.before(today);
    }

    public String generateTokenForUserId(@NonNull final String userId) {
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
}

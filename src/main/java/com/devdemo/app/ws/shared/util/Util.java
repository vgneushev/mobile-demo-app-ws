package com.devdemo.app.ws.shared.util;

import com.devdemo.app.ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
}

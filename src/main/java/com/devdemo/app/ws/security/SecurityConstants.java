package com.devdemo.app.ws.security;

import com.devdemo.app.ws.SpringApplicationContext;
import org.springframework.core.env.Environment;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; //10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    public static final String USER_ID_HEADER = "UserId ";
    public static final String SIGN_UP_URL = "/users";

    public static String getTokenSecret() {
        Environment environment = (Environment) SpringApplicationContext.getBean("environment");
        return environment.getProperty("tokenSecret");
    }
}

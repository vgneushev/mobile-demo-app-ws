package com.devdemo.app.ws.security;

import com.devdemo.app.ws.SpringApplicationContext;
import org.springframework.core.env.Environment;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; //10 days
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000; //1 hour
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    public static final String USER_ID_HEADER = "UserID";
    public static final String SIGN_UP_URL = "/users";
    public static final String USERS_URL = "/users/**";
    public static final String LOGIN_URL = "/users/login";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String H2_CONSOLE_URL = "/h2-console/**";
    public static final String RESET_PASSWORD_REQUEST_URL = "/users/password-reset-request";
    public static final String RESET_PASSWORD_URL = "/users/password-reset";


    public static String getTokenSecret() {
        Environment environment = (Environment) SpringApplicationContext.getBean("environment");
        return environment.getProperty("tokenSecret");
    }
}

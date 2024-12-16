package com.devdemo.app.ws.shared.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum SpringDocUrl {
    API_V3("/v3/api-docs/**"),
    CONFIG("/configuration/**"),
    SWAGGER("/swagger-ui*/**"),
    WEBJAR("/webjars/**");

    @Getter
    private final String url;
}

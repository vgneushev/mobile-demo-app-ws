package com.devdemo.app.ws;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class SpringDocConfig {
    @Bean
    public GroupedOpenApi controllerApi() {
        return GroupedOpenApi.builder()
                .group("controller-api")
                .packagesToScan("com.devdemo.app.ws") // Specify the package to scan
                .build();
    }
}

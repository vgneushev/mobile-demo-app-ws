package com.devdemo.app.ws;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class SpringDocConfig {
    private Contact getContact() {
        Contact contact = new Contact();
        contact.setName("vgneushev");
        contact.setEmail("vg");
        return contact;
    }

    private Info getApiInfo() {
        Info apiInfo = new Info();
        apiInfo.contact(getContact());
        apiInfo.description("User Details CRUD operations");
        apiInfo.setVersion("1.0");
        apiInfo.setSummary("Swagger Open API Specification for photo_app");
        apiInfo.setTitle("Photo_App");
        return apiInfo;
    }
    @Bean
    public GroupedOpenApi controllerApi() {
        return GroupedOpenApi.builder()
                .group("controller-api")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(getApiInfo()))
                .displayName("DisplayName")
                .packagesToScan("com.devdemo.app.ws") // Specify the package to scan
                .build();
    }
}

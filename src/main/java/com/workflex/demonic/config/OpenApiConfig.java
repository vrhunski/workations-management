package com.workflex.demonic.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI workflexOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WorkFlex Workation API")
                        .description("REST API for managing workation trips")
                        .version("1.0.0"));
    }
}

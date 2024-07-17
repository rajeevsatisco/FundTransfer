package com.api.fundtransfer.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.api.fundtransfer.constant.Constants.*;
/**
 * Configuration class for Swagger/OpenAPI documentation.
 */
@Configuration
public class SwaggerConfig {
    /**
     * Bean definition for OpenAPI configuration.
     *
     * @return a customized OpenAPI object with application-specific metadata.
     */
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(TITLE)
                        .description(APPLICATION_NAME)
                        .version(VERSION)
                        .license(new License().name(LICENSE).url(URL)));
    }
}

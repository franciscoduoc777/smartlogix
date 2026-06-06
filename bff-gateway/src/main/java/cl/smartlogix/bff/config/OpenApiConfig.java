package cl.smartlogix.bff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bffOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SMARTLOGIX - BFF Gateway")
                        .description("Backend For Frontend - agregación y proxy")
                        .version("1.0.0"));
    }
}

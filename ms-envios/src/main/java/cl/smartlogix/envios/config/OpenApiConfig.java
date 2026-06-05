package cl.smartlogix.envios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI enviosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SMARTLOGIX - MS Envíos")
                        .description("API REST del microservicio de envíos")
                        .version("1.0.0"));
    }
}

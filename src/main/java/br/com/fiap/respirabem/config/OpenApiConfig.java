package br.com.fiap.respirabem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("RespiraBem API")
                .version("1.0.0")
                .description("Microsserviço do RespiraBem (ODS 3 - Saúde e Bem-Estar): cidades, "
                        + "leituras da qualidade do ar, grupos sensíveis e recomendações de saúde."));
    }
}

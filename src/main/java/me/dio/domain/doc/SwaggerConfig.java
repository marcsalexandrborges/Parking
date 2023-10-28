package me.dio.domain.doc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Nome da API",
                version = "1.0",
                description = "Descrição da API"
        )
)

@Configuration
public class SwaggerConfig {
    // Configurações do Swagger
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api")
                .packagesToScan("me.dio.domain.controller")
                .build();
    }

}

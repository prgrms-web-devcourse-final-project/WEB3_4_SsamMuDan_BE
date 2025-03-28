package ssammudan.cotree.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info = @Info(title = "API 서버", version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi groupApiV1() {
        return GroupedOpenApi.builder()
                             .group("apiV1")
                             .pathsToMatch("/api/v1/**")
                             .build();
    }

    @Bean
    public GroupedOpenApi groupController() {
        return GroupedOpenApi.builder()
                             .group("controller")
                             .pathsToExclude("/api/**")
                             .build();
    }

}
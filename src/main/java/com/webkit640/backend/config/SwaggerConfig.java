package com.webkit640.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String REFERENCE = "Bearer ";

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Webkit640")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.ant("/**"))
                .build()
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(securityScheme()));
    }

    private ApiKey securityScheme() {
        String targetHeader = "Authorization";
        return new ApiKey(REFERENCE, targetHeader, "header");
    }

    private SecurityContext securityContext() {
        return springfox.documentation
                .spi.service.contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth())
                .operationSelector(operationContext -> true)
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return List.of(new SecurityReference(REFERENCE, authorizationScopes));
    }

    private HttpAuthenticationScheme bearerAuthSecurityScheme() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name(REFERENCE).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("SONG MIN GYU")
                .description("Webkit640 Backend Refactoring Project API Documentation")
                .termsOfServiceUrl("https://github.com/SongMinGyu0506/Webkit640_back_refactoring")
                .license("https://github.com/SongMinGyu0506")
                .licenseUrl("https://github.com/SongMinGyu0506").version("1.0").build();
    }
}

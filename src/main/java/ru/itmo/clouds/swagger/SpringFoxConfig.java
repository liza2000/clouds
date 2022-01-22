package ru.itmo.clouds.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.*;

@Configuration
@EnableSwagger2
class SpringFoxConfig extends WebMvcConfigurationSupport {
    private String API_VERSION = "0.0.1";

    @Bean
    public Docket  api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securityContexts(new ArrayList<SecurityContext>(){{add(securityContext());}})
            .securitySchemes(new ArrayList<ApiKey>(){{add(apiKey());}})
            .select()
            .apis(RequestHandlerSelectors.basePackage("ru.itmo.clouds.controller"))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiKey apiKey()  {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext()  {
        return SecurityContext.builder()
            .securityReferences(new ArrayList<SecurityReference>(){{add(defaultAuth());}})
            .build();
    }

    private SecurityReference defaultAuth() {
        return SecurityReference.builder()
            .scopes(new AuthorizationScope[]{})
            .reference("JWT")
            .build();
    }

    protected void addResourceHandlers(ResourceHandlerRegistry registry ) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    private ApiInfo apiInfo(){
        return new ApiInfo(
            "Версионирование",
            "Версионирование датасетов",
            API_VERSION,
            "Terms of service",
           new  Contact("", "", ""),
            "License of API",
            "API license URL",
            Collections.emptyList());
        }
}
package com.adole.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: adole
 * @Date: 2020/1/11 18:00
 */
@Configuration
@EnableSwagger2
public class Swagger2Config
{
    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.adole"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo createApiInfo() {
        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("基础系统API")
                .description("=web API")
                .termsOfServiceUrl("")
                .contact("framework")
                .version("1.0");

        return builder.build();
    }
}
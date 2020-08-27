package com.l1yp.conf;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.lang.reflect.WildcardType;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Resource
    private TypeResolver typeResolver;


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.l1yp.controller"))
                .paths(PathSelectors.any())
                .build()
                .alternateTypeRules(
                        AlternateTypeRules.newRule(typeResolver.resolve(DeferredResult.class, typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RedisTool API SWAGGER DOC")
                .version("1.0")
                .build();
    }
}

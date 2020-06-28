package com.usian.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2//开启swagger
public class SwaggerConfig {

    /**
     * Docket：生成接口文档
     * @return
     */
    @Bean
    public Docket getDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.usian.controller"))//要扫描的包
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
         return new ApiInfoBuilder()
                .title("优思安商城后台管理系统")//标题
                .description("商品管理接模块接口文档")//描述
                .version("1.0")
                .build();
    }
}

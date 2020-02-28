package com.nidotim.lottery.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@ComponentScan(basePackages = {"com.nidotim.lottery"})
//@ComponentScan(basePackageClasses = {
  //  LotteryController.class
//})
@EnableWebMvc
public class SpringFoxConfig extends WebMvcConfigurerAdapter {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        //.apis(RequestHandlerSelectors.any())
        .apis(RequestHandlerSelectors.basePackage("com.nidotim.lottery.controller"))
        //.apis(Predicates.not(RequestHandlerSelectors.basePackage("com.nidotim")))
        // .paths(PathSelectors.any())
        //.paths(PathSelectors.ant("/swagger2-demo"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
    // @formatter:on
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "My REST API",
        "Some custom description of API.",
        "API TOS",
        "Terms of service",
        new Contact("John Doe", "www.example.com", "myeaddress@company.com"),
        "License of API", "API license URL", Collections.emptyList());
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry)  {
    //enabling swagger-ui part for visual documentation
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

}
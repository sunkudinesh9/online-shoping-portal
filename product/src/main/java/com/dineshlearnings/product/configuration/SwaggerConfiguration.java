package com.dineshlearnings.product.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.dineshlearnings.product"))
				.build()
				.apiInfo(apiInfo());

	}
	
	private ApiInfo apiInfo() {
		return new ApiInfo("Product Microservice", 
				"This service give the details of the products",
				"1.0",
				"https://github.com/sunkudinesh9/online-shoping-portal", 
				new Contact("Sunku Dinesh Kumar", "https://github.com/sunkudinesh9", "sunku.dinesh9@gmail.com"),
				"",
				"",
				Collections.emptyList());
	}
}

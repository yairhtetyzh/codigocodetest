package com.demo.codetest.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
	    info = @Info(
	        title = "Code Test API",
	        version = "v1",
	        description = "API documentation for the Code Test"
	    )
	)
public class SwaggerConfig {
}

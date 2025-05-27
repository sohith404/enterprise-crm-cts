package com.crm.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info; 

@Configuration
public class SwaggerDocumentation {

	
	@Bean
	public OpenAPI customerOpenAPI() {
		return new OpenAPI().info(new Info().title("Employee Rest API")
				.description("API Documentation")
				.version("v1.0"));
	}
}

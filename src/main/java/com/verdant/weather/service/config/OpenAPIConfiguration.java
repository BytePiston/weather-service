package com.verdant.weather.service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfiguration {

	@Bean
	public OpenAPI defineOpenApi() {
		Server server = new Server();
		server.setUrl("http://bytemetwice.ninja");
		server.setDescription("Development");

		Contact myContact = new Contact();
		myContact.setName("Neel Priyadarshi");
		myContact.setEmail("neel.priyadarshi18@gmail.com");

		Info information = new Info().title("Weather Service API Documentation")
			.version("1.0")
			.description("API endpoints to query weather using OpenWeatherMap API.")
			.contact(myContact);
		return new OpenAPI().info(information).servers(List.of(server));
	}

}

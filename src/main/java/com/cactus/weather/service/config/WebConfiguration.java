package com.cactus.weather.service.config;

import com.cactus.weather.service.cache.CachedWeatherMap;
import com.cactus.weather.service.cache.CachedWeatherMapInterface;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Getter
@Configuration
public class WebConfiguration {

	@Value("${openweathermap.api.key}")
	private String openWeatherMapApiKey;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/***************************************************************************************
	 * This is the configuration for the cache that stores the weather data for a query
	 * URL as the key and the Pair as the value. The Pair represents a key-value pair of
	 * TTL and WeatherResponse. TTL is calculated in milliseconds based on the generated
	 * time of the weather data + 10 minutes as refresh data time specified by
	 * OpenWeatherMapAPI Docs.
	 **************************************************************************************/
	@Bean
	@Scope("singleton")
	public CachedWeatherMapInterface cachedWeatherMap() {
		return new CachedWeatherMap();
	}

}

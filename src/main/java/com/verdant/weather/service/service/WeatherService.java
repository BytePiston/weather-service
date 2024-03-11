package com.verdant.weather.service.service;

import com.verdant.weather.service.exception.ResourceNotFoundException;
import com.verdant.weather.service.model.response_model.WeatherResponse;

import java.util.Optional;

public interface WeatherService {

	// Fetch current weather by city id
	Optional<WeatherResponse> getCurrentWeatherByCityId(String cityId) throws ResourceNotFoundException;

	// Fetch current weather by city name
	Optional<WeatherResponse> getCurrentWeatherByCityName(String cityName) throws ResourceNotFoundException;

	// Fetch current weather by city name and state code
	Optional<WeatherResponse> getCurrentWeatherByCityNameAndCountryCode(String cityName, String stateCode)
			throws ResourceNotFoundException;

	// Fetch current weather by city name and state code
	Optional<WeatherResponse> getCurrentWeatherByCityNameAndStateCodeAndCountryCode(String cityName, String stateCode,
			String countryCode) throws ResourceNotFoundException;

	// Fetch current weather by zip code
	Optional<WeatherResponse> getCurrentWeatherByZipCode(String zip) throws ResourceNotFoundException;

	// Fetch current weather by zip code and country code
	Optional<WeatherResponse> getCurrentWeatherByZipCodeAndCountryCode(String zipCode, String countryCode)
			throws ResourceNotFoundException;

	// Fetch current weather by coordinates
	Optional<WeatherResponse> getCurrentWeatherByCoordinates(Double latitude, Double longitude)
			throws ResourceNotFoundException;

}

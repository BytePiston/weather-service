package com.verdant.weather.service.validator;

import com.verdant.weather.service.exception.ParameterValidationException;

import java.util.regex.Pattern;

/**
 * Validator for GET request parameters to validate the input request parameters;
 */
public class RequestValidationUtil {

	public static void validateCityName(String cityName) {
		if (cityName == null || cityName.trim().isEmpty()) {
			throw new ParameterValidationException("City name is required");
		}
		if (!Pattern.matches("^[a-zA-Z\\s]+", cityName)) {
			throw new ParameterValidationException("City name must contain only letters and spaces");
		}
	}

	public static void validateZipCode(String zipCode) {
		if (zipCode == null || zipCode.trim().isEmpty()) {
			throw new ParameterValidationException("Zip code is required");
		}
		if (!Pattern.matches("^[a-zA-Z0-9]{1,10}$", zipCode)) {
			throw new ParameterValidationException(
					"Zip code must be a between 1 and 10 characters long and contain only letters and numbers");
		}
	}

	public static void validateCoordinate(Double latitude, Double longitude) {
		validateLatitude(latitude);
		validateLongitude(longitude);
	}

	private static void validateLatitude(Double latitude) {
		if (latitude == null) {
			throw new ParameterValidationException("Latitude is required");
		}
		if (latitude < -90 || latitude > 90) {
			throw new ParameterValidationException("Latitude must be a number between -90 and 90");
		}
	}

	public static void validateLongitude(Double longitude) {
		if (longitude == null) {
			throw new ParameterValidationException("Longitude is required");
		}
		if (longitude < -180 || longitude > 180) {
			throw new ParameterValidationException("Longitude must be a number between -180 and 180");
		}
	}

	public static void validateStateCode(String stateCode) {
		if (stateCode == null || stateCode.trim().isEmpty()) {
			throw new ParameterValidationException("State code is required");
		}
		if (!Pattern.matches("^[a-zA-Z]{2}$", stateCode)) {
			throw new ParameterValidationException("State code must be a two-letter code");
		}
	}

	public static void validateCountryCode(String countryCode) {
		if (countryCode == null || countryCode.trim().isEmpty()) {
			throw new ParameterValidationException("Country code is required");
		}
		if (!Pattern.matches("^[a-zA-Z]{2}$", countryCode)) {
			throw new ParameterValidationException("Country code must be a two-letter code");
		}
	}

	public static void validateCityId(String cityId) {
		if (cityId == null || cityId.trim().isEmpty()) {
			throw new ParameterValidationException("City ID is required");
		}
		if (!Pattern.matches("^(0|[1-9]\\d*)$", cityId)) {
			throw new ParameterValidationException("City ID must be a digits.");
		}
	}

	public static void validatePageNumber(Integer page) {
		if (page < 0) {
			throw new ParameterValidationException("Page number must be a positive number");
		}
	}

	public static void validatePageSize(Integer pageSize) {
		if (pageSize < 1) {
			throw new ParameterValidationException("Page size must be a positive number");
		}
		if (pageSize > 100) {
			throw new ParameterValidationException("Page size must be less than or equal to 100");
		}
	}

}

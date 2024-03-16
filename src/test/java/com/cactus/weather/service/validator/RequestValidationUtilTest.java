package com.cactus.weather.service.validator;

import com.cactus.weather.service.exception.ParameterValidationException;
import com.cactus.weather.service.validator.RequestValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestValidationUtilTest {

	@Test
	@DisplayName("Positive Scenario: Validate City Name")
	void validateCityNamePositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validateCityName("New York"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateCityName("Los Angeles"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateCityName("San Francisco"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateCityName("Tokyo"));
	}

	@Test
	@DisplayName("Negative Scenario: Validate City Name")
	void validateCityNameNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityName(null));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityName(""));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityName(" "));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityName("New York1"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityName("New York!"));
		assertEquals("City name must contain only letters and spaces", assertThrows(ParameterValidationException.class,
				() -> RequestValidationUtil.validateCityName("New York1"))
			.getMessage());
	}

	@Test
	@DisplayName("Positive Scenario: Validate Zip Code")
	void validateZipCodePositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validateZipCode("12345"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateZipCode("1234567890"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateZipCode("A2C4W5"));
	}

	@Test
	@DisplayName("Negative Scenario: Validate Zip Code")
	void validateZipCodeNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateZipCode(null));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateZipCode(""));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateZipCode(" "));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateZipCode("12345678901"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateZipCode("12345!"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateZipCode("ab21-a31a"));
		assertEquals("Zip code must be a between 1 and 10 characters long and contain only letters and numbers",
				assertThrows(ParameterValidationException.class,
						() -> RequestValidationUtil.validateZipCode("ab21-a31a"))
					.getMessage());
	}

	@Test
	@DisplayName("Positive Scenario: Validate Coordinate")
	void validateCoordinatePositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validateCoordinate(12.345, 123.456));
		assertDoesNotThrow(() -> RequestValidationUtil.validateCoordinate(-12.345, -123.456));
		assertDoesNotThrow(() -> RequestValidationUtil.validateCoordinate(0.0, 0.0));
	}

	@Test
	@DisplayName("Negative Scenario: Validate Coordinate")
	void validateCoordinateNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCoordinate(null, null));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCoordinate(null, 123.456));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCoordinate(12.345, null));
		assertThrows(ParameterValidationException.class,
				() -> RequestValidationUtil.validateCoordinate(12.345, 181.456));
		assertThrows(ParameterValidationException.class,
				() -> RequestValidationUtil.validateCoordinate(91.345, 123.456));
		assertEquals("Latitude must be a number between -90 and 90", assertThrows(ParameterValidationException.class,
				() -> RequestValidationUtil.validateCoordinate(91.345, 123.456))
			.getMessage());
	}

	@Test
	@DisplayName("Positive Scenario: Validate Longitude")
	void validateLongitudePositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validateLongitude(123.456));
		assertDoesNotThrow(() -> RequestValidationUtil.validateLongitude(-123.456));
		assertDoesNotThrow(() -> RequestValidationUtil.validateLongitude(0.0));
	}

	@Test
	@DisplayName("Negative Scenario: Validate Longitude")
	void validateLongitudeNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateLongitude(null));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateLongitude(181.456));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateLongitude(-181.456));
		assertEquals("Longitude must be a number between -180 and 180", assertThrows(ParameterValidationException.class,
				() -> RequestValidationUtil.validateLongitude(-181.456))
			.getMessage());
	}

	@Test
	@DisplayName("Positive Scenario: Validate Latitude")
	void validateStateCodePositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validateStateCode("NY"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateStateCode("CA"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateStateCode("TX"));
	}

	@Test
	@DisplayName("Negative Scenario: Validate State Code")
	void validateStateCodeNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateStateCode(null));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateStateCode(""));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateStateCode(" "));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateStateCode("New York"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateStateCode("NYC"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateStateCode("N1"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateStateCode("NY!"));
		assertEquals("State code must be a two-letter code",
				assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateStateCode("NYC"))
					.getMessage());
	}

	@Test
	@DisplayName("Positive Scenario: Validate Country Code")
	void validateCountryCodePositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validateCountryCode("US"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateCountryCode("CA"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateCountryCode("IN"));
	}

	@Test
	@DisplayName("Negative Scenario: Validate Country Code")
	void validateCountryCodeNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCountryCode(null));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCountryCode(""));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCountryCode(" "));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCountryCode("USA"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCountryCode("US1"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCountryCode("US!"));
		assertEquals("Country code must be a two-letter code",
				assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCountryCode("USA"))
					.getMessage());
	}

	@Test
	@DisplayName("Positive Scenario: Validate City Id")
	void validateCityIdPositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validateCityId("12345"));
		assertDoesNotThrow(() -> RequestValidationUtil.validateCityId("1234567890"));
	}

	@Test
	@DisplayName("Negative Scenario: Validate City Id")
	void validateCityIdNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityId(null));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityId(""));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityId(" "));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityId("-12345"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityId("12345!"));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validateCityId("ab21-a31a"));
		assertEquals("City ID must be a digits.", assertThrows(ParameterValidationException.class,
				() -> RequestValidationUtil.validateCityId("ab21-a31a"))
			.getMessage());
	}

	@Test
	@DisplayName("Positive Scenario: Validate Page Number")
	void validatePageNumberPositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validatePageNumber(0));
		assertDoesNotThrow(() -> RequestValidationUtil.validatePageNumber(1));
		assertDoesNotThrow(() -> RequestValidationUtil.validatePageNumber(10));
	}

	@Test
	@DisplayName("Negative Scenario: Validate Page Number")
	void validatePageNumberNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validatePageNumber(-1));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validatePageNumber(-10));
		assertEquals("Page number must be a positive number",
				assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validatePageNumber(-1))
					.getMessage());
	}

	@Test
	@DisplayName("Positive Scenario: Validate Page Size")
	void validatePageSizePositiveScenario() {
		assertDoesNotThrow(() -> RequestValidationUtil.validatePageSize(1));
		assertDoesNotThrow(() -> RequestValidationUtil.validatePageSize(10));
		assertDoesNotThrow(() -> RequestValidationUtil.validatePageSize(100));
	}

	@Test
	@DisplayName("Negative Scenario: Validate Page Size")
	void validatePageSizeNegativeScenario() {
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validatePageSize(0));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validatePageSize(-1));
		assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validatePageSize(-10));
		assertEquals("Page size must be a positive number",
				assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validatePageSize(0))
					.getMessage());
		assertEquals("Page size must be less than or equal to 100",
				assertThrows(ParameterValidationException.class, () -> RequestValidationUtil.validatePageSize(101))
					.getMessage());
	}

}

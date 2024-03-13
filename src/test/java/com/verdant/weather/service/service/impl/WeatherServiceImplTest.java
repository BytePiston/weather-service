package com.verdant.weather.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verdant.weather.service.cache.CachedWeatherMap;
import com.verdant.weather.service.cache.CachedWeatherMapInterface;
import com.verdant.weather.service.config.WebConfiguration;
import com.verdant.weather.service.exception.ResourceNotFoundException;
import com.verdant.weather.service.model.response_model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.verdant.weather.service.util.OpenWeatherMapApiUtils.STATUS_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
class WeatherServiceImplTest {

	@Mock
	private WebConfiguration webConfig;

	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private CachedWeatherMapInterface cachedWeatherMap;

	private ObjectMapper objectMapper = new ObjectMapper();

	private static String naplesWeatherData;

	private static String beverlyWeatherData;

	private static String quanticoWeatherData;

	@MockBean
	ApplicationEventPublisher mockEventPublisher;

	private WeatherServiceImpl weatherServiceImpl;

	@BeforeEach
	void setUp() throws IOException {
		MockitoAnnotations.initMocks(this);
		weatherServiceImpl = new WeatherServiceImpl(webConfig, new CachedWeatherMap(), restTemplate,
				mockEventPublisher);
		// mockEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
		naplesWeatherData = objectMapper.readTree(new File("src/test/resources/weather_data/Naples.json")).toString();
		beverlyWeatherData = objectMapper.readTree(new File("src/test/resources/weather_data/Beverly_Hills.json"))
			.toString();
		quanticoWeatherData = objectMapper.readTree(new File("src/test/resources/weather_data/Quantico.json"))
			.toString();

		when(webConfig.getOpenWeatherMapApiKey()).thenReturn("1234567890");
		when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(naplesWeatherData.toString());
	}

	@Test
	@DisplayName("Positive Scenario: Fetch Weather By City Id")
	void testGetCurrentWeatherByCityIdPositiveScenario() throws ResourceNotFoundException {
		Optional<WeatherResponse> weatherResponseOptional = weatherServiceImpl.getCurrentWeatherByCityId("1234567890");
		WeatherResponse weatherResponse = weatherResponseOptional.get();
		assertNotNull(weatherResponse);
		assertEquals(STATUS_SUCCESS, weatherResponse.getMessage());
		assertNotNull(weatherResponse.getCurrentWeather());
		assertEquals(200, weatherResponse.getCurrentWeather().getCod());
		assertEquals(0, weatherResponse.getCurrentWeather().getId());
		assertEquals("Naples", weatherResponse.getCurrentWeather().getName());
		assertEquals("US", weatherResponse.getCurrentWeather().getSys().getCountry());
	}

	@Test
    @DisplayName("Negative Scenario: Fetch Weather By City Id")
    void testGetCurrentWeatherByCityIdNegativeScenario() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Test");
        assertThrows(ResourceNotFoundException.class, () -> weatherServiceImpl.getCurrentWeatherByCityId("1234567890"));
    }

	@Test
    @DisplayName("Positive Scenario: Fetch Weather By City Name")
    void testGetCurrentWeatherByCityNamePositiveScenario() throws ResourceNotFoundException {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(beverlyWeatherData);
        Optional<WeatherResponse> weatherResponseOptional = weatherServiceImpl.getCurrentWeatherByCityName("Beverly Hills");
        WeatherResponse weatherResponse = weatherResponseOptional.get();
        assertNotNull(weatherResponse);
        assertEquals(STATUS_SUCCESS, weatherResponse.getMessage());
        assertNotNull(weatherResponse.getCurrentWeather());
        assertEquals(200, weatherResponse.getCurrentWeather().getCod());
        assertEquals(0, weatherResponse.getCurrentWeather().getId());
        assertEquals("Beverly Hills", weatherResponse.getCurrentWeather().getName());
        assertEquals("US", weatherResponse.getCurrentWeather().getSys().getCountry());
    }

	@Test
    @DisplayName("Negative Scenario: Fetch Weather By City Name")
    void testGetCurrentWeatherByCityNameNegativeScenario() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Test");
        assertThrows(ResourceNotFoundException.class, () -> weatherServiceImpl.getCurrentWeatherByCityName("Beverly Hills"));
    }

	@Test
    @DisplayName("Positive Scenario: Fetch Weather By City Name And Country Code")
    void testGetCurrentWeatherByCityNameAndCountryCodePositiveScenario() throws ResourceNotFoundException {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(quanticoWeatherData);
        Optional<WeatherResponse> weatherResponseOptional = weatherServiceImpl.getCurrentWeatherByCityNameAndCountryCode("Quantico", "US");
        WeatherResponse weatherResponse = weatherResponseOptional.get();
        assertNotNull(weatherResponse);
        assertEquals(STATUS_SUCCESS, weatherResponse.getMessage());
        assertNotNull(weatherResponse.getCurrentWeather());
        assertEquals(200, weatherResponse.getCurrentWeather().getCod());
        assertEquals(0, weatherResponse.getCurrentWeather().getId());
        assertEquals("Quantico", weatherResponse.getCurrentWeather().getName());
        assertEquals("US", weatherResponse.getCurrentWeather().getSys().getCountry());
    }

	@Test
    @DisplayName("Negative Scenario: Fetch Weather By City Name And Country Code")
    void testGetCurrentWeatherByCityNameAndCountryCodeNegativeScenario() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Test");
        assertThrows(ResourceNotFoundException.class, () -> weatherServiceImpl.getCurrentWeatherByCityNameAndCountryCode("Quantico", "US"));
    }

	@Test
    @DisplayName("Positive Scenario: Fetch Weather By City Name And State Code And Country Code")
    void testGetCurrentWeatherByCityNameAndStateCodeAndCountryCodePositiveScenario() throws ResourceNotFoundException {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(quanticoWeatherData);
        Optional<WeatherResponse> weatherResponseOptional = weatherServiceImpl.getCurrentWeatherByCityNameAndStateCodeAndCountryCode("Quantico", "VA", "US");
        WeatherResponse weatherResponse = weatherResponseOptional.get();
        assertNotNull(weatherResponse);
        assertEquals(STATUS_SUCCESS, weatherResponse.getMessage());
        assertNotNull(weatherResponse.getCurrentWeather());
        assertEquals(200, weatherResponse.getCurrentWeather().getCod());
        assertEquals(0, weatherResponse.getCurrentWeather().getId());
        assertEquals("Quantico", weatherResponse.getCurrentWeather().getName());
        assertEquals("US", weatherResponse.getCurrentWeather().getSys().getCountry());
    }

	@Test
    @DisplayName("Negative Scenario: Fetch Weather By City Name And State Code And Country Code")
    void testGetCurrentWeatherByCityNameAndStateCodeAndCountryCodeNegativeScenario() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Test");
        assertThrows(ResourceNotFoundException.class, () -> weatherServiceImpl.getCurrentWeatherByCityNameAndStateCodeAndCountryCode("Quantico", "VA", "US"));
    }

	@Test
    @DisplayName("Positive Scenario: Fetch Weather By Zip Code")
    void testGetCurrentWeatherByZipCodePositiveScenario() throws ResourceNotFoundException {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(naplesWeatherData);
        Optional<WeatherResponse> weatherResponseOptional = weatherServiceImpl.getCurrentWeatherByZipCode("34102");
        WeatherResponse weatherResponse = weatherResponseOptional.get();
        assertNotNull(weatherResponse);
        assertEquals(STATUS_SUCCESS, weatherResponse.getMessage());
        assertNotNull(weatherResponse.getCurrentWeather());
        assertEquals(200, weatherResponse.getCurrentWeather().getCod());
        assertEquals(0, weatherResponse.getCurrentWeather().getId());
        assertEquals("Naples", weatherResponse.getCurrentWeather().getName());
        assertEquals("US", weatherResponse.getCurrentWeather().getSys().getCountry());
    }

	@Test
    @DisplayName("Negative Scenario: Fetch Weather By Zip Code")
    void testGetCurrentWeatherByZipCodeNegativeScenario() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Test");
        assertThrows(ResourceNotFoundException.class, () -> weatherServiceImpl.getCurrentWeatherByZipCode("34102"));
    }

	@Test
    @DisplayName("Positive Scenario: Fetch Weather By Zip Code And Country Code")
    void testGetCurrentWeatherByZipCodeAndCountryCodePositiveScenario() throws ResourceNotFoundException {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(naplesWeatherData);
        Optional<WeatherResponse> weatherResponseOptional = weatherServiceImpl.getCurrentWeatherByZipCodeAndCountryCode("34102", "US");
        WeatherResponse weatherResponse = weatherResponseOptional.get();
                assertNotNull(weatherResponse);
        assertEquals(STATUS_SUCCESS, weatherResponse.getMessage());
        assertNotNull(weatherResponse.getCurrentWeather());
        assertEquals(200, weatherResponse.getCurrentWeather().getCod());
        assertEquals(0, weatherResponse.getCurrentWeather().getId());
        assertEquals("Naples", weatherResponse.getCurrentWeather().getName());
        assertEquals("US", weatherResponse.getCurrentWeather().getSys().getCountry());
    }

	@Test
    @DisplayName("Negative Scenario: Fetch Weather By Zip Code And Country Code")
    void testGetCurrentWeatherByZipCodeAndCountryCodeNegativeScenario() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Test");
        assertThrows(ResourceNotFoundException.class, () -> weatherServiceImpl.getCurrentWeatherByZipCodeAndCountryCode("34102", "US"));
    }

	@Test
    @DisplayName("Positive Scenario: Fetch Weather By Coordinates")
    void testGetCurrentWeatherByCoordinatesPositiveScenario() throws ResourceNotFoundException {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(naplesWeatherData);
        Optional<WeatherResponse> weatherResponseOptional = weatherServiceImpl.getCurrentWeatherByCoordinates(26.142, -81.7948);
        WeatherResponse weatherResponse = weatherResponseOptional.get();
        assertNotNull(weatherResponse);
        assertEquals(STATUS_SUCCESS, weatherResponse.getMessage());
        assertNotNull(weatherResponse.getCurrentWeather());
        assertEquals(200, weatherResponse.getCurrentWeather().getCod());
        assertEquals(0, weatherResponse.getCurrentWeather().getId());
        assertEquals("Naples", weatherResponse.getCurrentWeather().getName());
        assertEquals("US", weatherResponse.getCurrentWeather().getSys().getCountry());
    }

	@Test
    @DisplayName("Negative Scenario: Fetch Weather By Coordinates")
    void testGetCurrentWeatherByCoordinatesNegativeScenario() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Test");
        assertThrows(ResourceNotFoundException.class, () -> weatherServiceImpl.getCurrentWeatherByCoordinates(26.142, -81.7948));
    }

}

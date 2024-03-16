package com.cactus.weather.service.controller;

import com.cactus.weather.service.controller.CurrentWeatherController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cactus.weather.service.model.response_model.WeatherModel;
import com.cactus.weather.service.model.response_model.WeatherResponse;
import com.cactus.weather.service.service.WeatherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrentWeatherController.class)
class CurrentWeatherControllerTest {

	@MockBean
	private WeatherService weatherService;

	@Autowired
	private MockMvc mockMvc;

	private static final WeatherResponse naplesWeatherData = new WeatherResponse();

	private static final WeatherResponse beverlyWeatherData = new WeatherResponse();

	private static final WeatherResponse quanticoWeatherData = new WeatherResponse();

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeAll
	static void setUp() throws IOException {
		naplesWeatherData.setCurrentWeather(
				objectMapper.readValue(new File("src/test/resources/weather_data/Naples.json"), WeatherModel.class));
		beverlyWeatherData.setCurrentWeather(objectMapper
			.readValue(new File("src/test/resources/weather_data/Beverly_Hills.json"), WeatherModel.class));
		quanticoWeatherData.setCurrentWeather(
				objectMapper.readValue(new File("src/test/resources/weather_data/Quantico.json"), WeatherModel.class));
	}

	@Test
    @DisplayName("Positive Scenario: Get current weather by city name")
    void getCurrentWeatherByCityNamePositiveScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCityName("Naples")).thenReturn(Optional.of(naplesWeatherData));
        mockMvc.perform(get("/api/v1/current/weather/city?name=" + "Naples"))
                .andExpect(status().isOk());
    }

	@Test
    @DisplayName("Negative Scenario: Get current weather by city name")
    void getCurrentWeatherByCityNameNegativeScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCityName("Lagos")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/current/weather/city?name=" + "Lagos"))
                .andExpect(status().isNotFound());
    }

	@Test
    @DisplayName("Positive Scenario: Get current weather by city id")
    void getCurrentWeatherByCityIdPositiveScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCityId(anyString())).thenReturn(Optional.of(beverlyWeatherData));
        mockMvc.perform(get("/api/v1/current/weather/city?id=" + "4164138"))
                .andExpect(status().isOk());
    }

	@Test
    @DisplayName("Negative Scenario: Get current weather by city id")
    void getCurrentWeatherByCityIdNegativeScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCityId(anyString())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/current/weather/city?id=" + 0))
                .andExpect(status().is4xxClientError());
    }

	@Test
    @DisplayName("Positive Scenario: Get current weather by zip code")
    void getCurrentWeatherByZipCodePositiveScenario() throws Exception {
        when(weatherService.getCurrentWeatherByZipCode("22134")).thenReturn(Optional.of(quanticoWeatherData));
        mockMvc.perform(get("/api/v1/current/weather/zip?zipCode=" + "22134"))
                .andExpect(status().isOk());
    }

	@Test
    @DisplayName("Negative Scenario: Get current weather by zip code")
    void getCurrentWeatherByZipCodeNegativeScenario() throws Exception {
        when(weatherService.getCurrentWeatherByZipCode("22134")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/current/weather/zip?zipCode=" + "22134"))
                .andExpect(status().is4xxClientError());
    }

	@Test
    @DisplayName("Positive Scenario: Get current weather by coordinates")
    void getCurrentWeatherByCoordinatesPositiveScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCoordinates(80.7128, 80.0060)).thenReturn(Optional.of(naplesWeatherData));
        mockMvc.perform(get("/api/v1/current/weather/coordinates?latitude=" + 80.7128 + "&longitude=" + 80.0060))
                .andExpect(status().isOk());
    }

	@Test
    @DisplayName("Negative Scenario: Get current weather by coordinates")
    void getCurrentWeatherByCoordinatesNegativeScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCoordinates(180.7128, 90.0060)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/current/weather/coordinates?latitude=" + "180.7128" + "&longitude=" + "90.0060"))
                .andExpect(status().is4xxClientError());
    }

	@Test
    @DisplayName("Positive Scenario: Get current weather by city name and country code")
    void getCurrentWeatherByCityRequestModelPositiveScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCityNameAndCountryCode("Naples", "US")).thenReturn(Optional.of(naplesWeatherData));
        mockMvc.perform(post("/api/v1/current/weather/city").contentType("application/json").content("{\n" +
                        "\t\t\"name\":\"Naples\",\n" +
                        "\t\t\"countryCode\":\"US\"\n" +
                        "\t}"))
                .andExpect(status().isOk());
    }

	@Test
    @DisplayName("Negative Scenario: Get current weather by city name and country code")
    void getCurrentWeatherByCityRequestModelNegativeScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCityNameAndCountryCode("Lagos", "NG")).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/v1/current/weather/city").contentType("application/json").content("{\n" +
                        "\t\t\"name\":\"Lagos\",\n" +
                        "\t\t\"countryCode\":\"NG\"\n" +
                        "\t}"))
                .andExpect(status().is4xxClientError());
    }

	@Test
    @DisplayName("Positive Scenario: Get current weather by zip code and country code")
    void getCurrentWeatherByZipCodeRequestModelPositiveScenario() throws Exception {
        when(weatherService.getCurrentWeatherByZipCodeAndCountryCode("1234", "US")).thenReturn(Optional.of(naplesWeatherData));
        mockMvc.perform(post("/api/v1/current/weather/zip").contentType("application/json").content("{\n" +
                        "\t\t\"zipCode\":\"1234\",\n" +
                        "\t\t\"countryCode\":\"US\"\n" +
                        "\t}"))
                .andExpect(status().isOk());
    }

	@Test
    @DisplayName("Negative Scenario: Get current weather by zip code and country code")
    void getCurrentWeatherByZipCodeRequestModelNegativeScenario() throws Exception {
        when(weatherService.getCurrentWeatherByZipCodeAndCountryCode("Lagos", "NG")).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/v1/current/weather/zip").contentType("application/json").content("{\n" +
                        "\t\t\"zipCode\":\"1234\",\n" +
                        "\t\t\"countryCode\":\"NG\"\n" +
                        "\t}"))
                .andExpect(status().is4xxClientError());
    }

	@Test
    @DisplayName("Positive Scenario: Get current weather by zip code")
    void testGetCurrentWeatherByZipCodeRequestModelPositiveScenario() throws Exception {
        when(weatherService.getCurrentWeatherByZipCode("22134")).thenReturn(Optional.of(quanticoWeatherData));
        mockMvc.perform(post("/api/v1/current/weather/zip").contentType("application/json").content("{\n" +
                        "\t\t\"zipCode\":\"22134\"\n" +
                        "\t}"))
                .andExpect(status().isOk());
    }

	@Test
    @DisplayName("Negative Scenario: Get current weather by zip code")
    void testGetCurrentWeatherByZipCodeRequestModelNegativeScenario() throws Exception {
        when(weatherService.getCurrentWeatherByZipCode("22134")).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/v1/current/weather/zip").contentType("application/json").content("{\n" +
                        "\t\t\"zipCode\":\"22134\"\n" +
                        "\t}"))
                .andExpect(status().is4xxClientError());
    }

	@Test
    @DisplayName("Positive Scenario: Get current weather by coordinates Request Model")
    void testGetCurrentWeatherByCoordinatesRequestModelPositiveScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCoordinates(80.7128, 80.0060)).thenReturn(Optional.of(naplesWeatherData));
        mockMvc.perform(post("/api/v1/current/weather/coordinates").contentType("application/json").content("{\n" +
                        "\t\t\"latitude\":80.7128,\n" +
                        "\t\t\"longitude\":80.0060\n" +
                        "\t}"))
                .andExpect(status().isOk());
    }

	@Test
    @DisplayName("Negative Scenario: Get current weather by coordinates Request Model")
    void testGetCurrentWeatherByCoordinatesRequestModelNegativeScenario() throws Exception {
        when(weatherService.getCurrentWeatherByCoordinates(180.7128, 90.0060)).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/v1/current/weather/coordinates").contentType("application/json").content("{\n" +
                        "\t\t\"latitude\":180.7128,\n" +
                        "\t\t\"longitude\":90.0060\n" +
                        "\t}"))
                .andExpect(status().is4xxClientError());
    }

}

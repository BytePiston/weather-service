package com.cactus.weather.service.service.impl;

import static com.cactus.weather.service.util.OpenWeatherMapApiUtils.*;

import com.cactus.weather.service.cache.CachedWeatherMapInterface;
import com.cactus.weather.service.config.WebConfiguration;
import com.cactus.weather.service.event.CityTrackerEvent;
import com.cactus.weather.service.exception.ResourceNotFoundException;
import com.cactus.weather.service.model.response_model.WeatherModel;
import com.cactus.weather.service.model.response_model.WeatherResponse;
import com.cactus.weather.service.service.WeatherService;
import com.cactus.weather.service.util.CityTrackerMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

  private final WebConfiguration webConfig;

  private final CachedWeatherMapInterface currentWeatherUrlCache;

  private final RestTemplate restTemplate;

  // Event publisher to create async event to increment the request count;
  private final ApplicationEventPublisher eventPublisher;

  @Autowired
  public WeatherServiceImpl(WebConfiguration webConfig,
                            CachedWeatherMapInterface currentWeatherUrlCache,
                            RestTemplate restTemplate,
                            ApplicationEventPublisher eventPublisher) {
    this.webConfig = webConfig;
    this.currentWeatherUrlCache = currentWeatherUrlCache;
    this.restTemplate = restTemplate;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public Optional<WeatherResponse> getCurrentWeatherByCityId(String cityId)
      throws ResourceNotFoundException {
    log.debug("Fetching current weather by city id: {}", cityId);

    String searchQueryUrl = CURRENT_WEATHER_URL + QUERY_BY_CITY_ID;

    searchQueryUrl = searchQueryUrl.replace(QUERY_PLACEHOLDER_CITY_ID, cityId);
    searchQueryUrl = searchQueryUrl.replace(
        QUERY_PLACEHOLDER_API_KEY, webConfig.getOpenWeatherMapApiKey());

    return getWeatherFromOpenWeatherMapApi(searchQueryUrl, CityTrackerMode.ID);
  }

  @Override
  public Optional<WeatherResponse> getCurrentWeatherByCityName(String cityName)
      throws ResourceNotFoundException {

    log.debug("Fetching current weather by city name: {}", cityName);

    String searchQueryUrl =
        CURRENT_WEATHER_URL + QUERY_BY_CITY_NAME + QUERY_PARAM_UNITS_METRIC;

    searchQueryUrl =
        searchQueryUrl.replace(QUERY_PLACEHOLDER_CITY_NAME, cityName);
    searchQueryUrl = searchQueryUrl.replace(
        QUERY_PLACEHOLDER_API_KEY, webConfig.getOpenWeatherMapApiKey());

    return getWeatherFromOpenWeatherMapApi(searchQueryUrl,
                                           CityTrackerMode.NAME);
  }

  @Override
  public Optional<WeatherResponse>
  getCurrentWeatherByCityNameAndCountryCode(String cityName, String countryCode)
      throws ResourceNotFoundException {

    log.debug("Fetching current weather by city name and state code: {}, {}",
              cityName, countryCode);

    String searchQueryUrl =
        CURRENT_WEATHER_URL + QUERY_BY_CITY_COUNTRY + QUERY_PARAM_UNITS_METRIC;

    searchQueryUrl =
        searchQueryUrl.replace(QUERY_PLACEHOLDER_CITY_NAME, cityName);
    searchQueryUrl =
        searchQueryUrl.replace(QUERY_PLACEHOLDER_COUNTRY_CODE, countryCode);
    searchQueryUrl = searchQueryUrl.replace(
        QUERY_PLACEHOLDER_API_KEY, webConfig.getOpenWeatherMapApiKey());

    return getWeatherFromOpenWeatherMapApi(searchQueryUrl,
                                           CityTrackerMode.NAME);
  }

  @Override
  public Optional<WeatherResponse>
  getCurrentWeatherByCityNameAndStateCodeAndCountryCode(String cityName,
                                                        String stateCode,
                                                        String countryCode)
      throws ResourceNotFoundException {

    log.debug(
        "Fetching current weather by city name, state code and country code: {}, {}, {}",
        cityName, stateCode, countryCode);

    String searchQueryUrl = CURRENT_WEATHER_URL + QUERY_BY_CITY_STATE_COUNTRY +
                            QUERY_PARAM_UNITS_METRIC;

    searchQueryUrl =
        searchQueryUrl.replace(QUERY_PLACEHOLDER_CITY_NAME, cityName);
    searchQueryUrl =
        searchQueryUrl.replace(QUERY_PLACEHOLDER_STATE_CODE, stateCode);
    searchQueryUrl =
        searchQueryUrl.replace(QUERY_PLACEHOLDER_COUNTRY_CODE, countryCode);
    searchQueryUrl = searchQueryUrl.replace(
        QUERY_PLACEHOLDER_API_KEY, webConfig.getOpenWeatherMapApiKey());

    return getWeatherFromOpenWeatherMapApi(searchQueryUrl,
                                           CityTrackerMode.NAME);
  }

  @Override
  public Optional<WeatherResponse> getCurrentWeatherByZipCode(String zip)
      throws ResourceNotFoundException {
    log.debug("Fetching current weather by zip code: {}", zip);

    String searchQueryUrl =
        CURRENT_WEATHER_URL + QUERY_BY_ZIP + QUERY_PARAM_UNITS_METRIC;

    searchQueryUrl = searchQueryUrl.replace(QUERY_PLACEHOLDER_ZIP_CODE, zip);
    searchQueryUrl = searchQueryUrl.replace(
        QUERY_PLACEHOLDER_API_KEY, webConfig.getOpenWeatherMapApiKey());

    return getWeatherFromOpenWeatherMapApi(searchQueryUrl, CityTrackerMode.ZIP);
  }

  @Override
  public Optional<WeatherResponse>
  getCurrentWeatherByZipCodeAndCountryCode(String zipCode, String countryCode)
      throws ResourceNotFoundException {

    log.debug("Fetching current weather by zip code and country code: {}, {}",
              zipCode, countryCode);

    String searchQueryUrl =
        CURRENT_WEATHER_URL + QUERY_BY_ZIP_COUNTRY + QUERY_PARAM_UNITS_METRIC;

    searchQueryUrl =
        searchQueryUrl.replace(QUERY_PLACEHOLDER_ZIP_CODE, zipCode);
    searchQueryUrl =
        searchQueryUrl.replace(QUERY_PLACEHOLDER_COUNTRY_CODE, countryCode);
    searchQueryUrl = searchQueryUrl.replace(
        QUERY_PLACEHOLDER_API_KEY, webConfig.getOpenWeatherMapApiKey());

    return getWeatherFromOpenWeatherMapApi(searchQueryUrl, CityTrackerMode.ZIP);
  }

  @Override
  public Optional<WeatherResponse>
  getCurrentWeatherByCoordinates(Double latitude, Double longitude)
      throws ResourceNotFoundException {

    log.debug("Fetching current weather by coordinates: {}, {}", latitude,
              longitude);

    String searchQueryUrl =
        CURRENT_WEATHER_URL + QUERY_BY_COORDINATES + QUERY_PARAM_UNITS_METRIC;

    searchQueryUrl = searchQueryUrl.replace(QUERY_PLACEHOLDER_LATITUDE,
                                            String.valueOf(latitude));
    searchQueryUrl = searchQueryUrl.replace(QUERY_PLACEHOLDER_LONGITUDE,
                                            String.valueOf(longitude));
    searchQueryUrl = searchQueryUrl.replace(
        QUERY_PLACEHOLDER_API_KEY, webConfig.getOpenWeatherMapApiKey());

    return getWeatherFromOpenWeatherMapApi(searchQueryUrl,
                                           CityTrackerMode.COORDINATE);
  }

  // Method to fetch weather using OpenWeatherMap API based on the search query
  // URL;
  private Optional<WeatherResponse>
  getWeatherFromOpenWeatherMapApi(String searchQueryUrl, CityTrackerMode mode)
      throws ResourceNotFoundException {
    WeatherResponse weatherResponse;
    // Check if the currentWeatherQueryUrl is present in the cache and the TTL
    // has not expired;
    if (currentWeatherUrlCache.containsKey(searchQueryUrl)) {
      weatherResponse = currentWeatherUrlCache.get(searchQueryUrl).getValue();
    } else {
      try {
        String result = restTemplate.getForObject(searchQueryUrl, String.class);
        WeatherModel weather =
            new ObjectMapper().readValue(result, WeatherModel.class);
        weatherResponse = WeatherResponse.builder()
                              .currentWeather(weather)
                              .message(STATUS_SUCCESS)
                              .build();
        currentWeatherUrlCache.cacheWeatherData(searchQueryUrl,
                                                weatherResponse);
      } catch (HttpClientErrorException | JsonProcessingException e) {
        log.error(
            "Error fetching current weather. Please verify request and try again: {}",
            e.getMessage());
        throw new ResourceNotFoundException(e.getMessage(), e.getCause(), false,
                                            false);
      }
    }

    // Publish an async event to increment the request count by category for
    // tracker API;
    eventPublisher.publishEvent(new CityTrackerEvent(
        weatherResponse.getCurrentWeather().getName(), mode));
    return Optional.of(weatherResponse);
  }
}

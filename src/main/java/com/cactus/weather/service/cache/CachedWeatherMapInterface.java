package com.cactus.weather.service.cache;

import com.cactus.weather.service.model.response_model.WeatherResponse;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public interface CachedWeatherMapInterface extends Map<String, Pair<Long, WeatherResponse>> {

  /**
   * Caches the weather data for a specific query URL. The TTL for the weather data is calculated
   * based on the generated time of the weather data plus 10 minutes.
   *
   * @param currentWeatherQueryUrl the query URL for the current weather data
   * @param weatherResponse the weather data to be cached
   */
  void cacheWeatherData(String currentWeatherQueryUrl, WeatherResponse weatherResponse);

  /**
   * Retrieves the pair of TTL and weather data from the cache map for a specific query URL. If the
   * TTL has expired, the pair is removed from the cache and null is returned.
   *
   * @param currentWeatherQueryUrl the query URL for the current weather data
   * @return the pair of TTL and weather data, or null if the TTL has expired or the key is not
   *     present in the cache
   */
  Pair<Long, WeatherResponse> get(Object currentWeatherQueryUrl);
}

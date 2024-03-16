package com.cactus.weather.service.config;

import static com.cactus.weather.service.util.OpenWeatherMapApiUtils.CACHE_TTL_IN_MINUTES;
import static org.junit.jupiter.api.Assertions.*;

import com.cactus.weather.service.cache.CachedWeatherMap;
import com.cactus.weather.service.cache.CachedWeatherMapInterface;
import com.cactus.weather.service.model.response_model.WeatherResponse;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CachedWeatherMapTest {

  @Test
  @DisplayName("Positive scenario: Cache Weather Data")
  void getWeatherPositive() {
    CachedWeatherMapInterface cachedWeatherMap = new CachedWeatherMap();
    long ttl = System.currentTimeMillis() +
               TimeUnit.MINUTES.toMillis(CACHE_TTL_IN_MINUTES);
    cachedWeatherMap.put(
        "test_url",
        Pair.of(ttl, WeatherResponse.builder().message("TEST").build()));
    Pair<Long, WeatherResponse> value = cachedWeatherMap.get("test_url");
    assertNotNull(value);
  }

  @Test
  @DisplayName("Negative scenario: Cache Weather Data")
  void getWeatherNegative() {
    CachedWeatherMapInterface cachedWeatherMap = new CachedWeatherMap();
    long ttl = System.currentTimeMillis() -
               TimeUnit.MINUTES.toMillis(CACHE_TTL_IN_MINUTES);
    cachedWeatherMap.put(
        "test_url",
        Pair.of(ttl, WeatherResponse.builder().message("TEST").build()));
    Pair<Long, WeatherResponse> value = cachedWeatherMap.get("test_url");
    assertNull(value);
  }
}

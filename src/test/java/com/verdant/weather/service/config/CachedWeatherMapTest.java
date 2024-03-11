package com.verdant.weather.service.config;

import com.verdant.weather.service.model.response_model.WeatherResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static com.verdant.weather.service.util.OpenWeatherMapApiUtils.CACHE_TTL_IN_MINUTES;
import static org.junit.jupiter.api.Assertions.*;

class CachedWeatherMapTest {

	@Test
	@DisplayName("Positive scenario: Cache Weather Data")
	void getWeatherPositive() {
		CachedWeatherMap cachedWeatherMap = new CachedWeatherMap();
		long ttl = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(CACHE_TTL_IN_MINUTES);
		cachedWeatherMap.put("test_url", Pair.of(ttl, WeatherResponse.builder().message("TEST").build()));
		Pair<Long, WeatherResponse> value = cachedWeatherMap.get("test_url");
		assertNotNull(value);
	}

	@Test
	@DisplayName("Negative scenario: Cache Weather Data")
	void getWeatherNegative() {
		CachedWeatherMap cachedWeatherMap = new CachedWeatherMap();
		long ttl = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(CACHE_TTL_IN_MINUTES);
		cachedWeatherMap.put("test_url", Pair.of(ttl, WeatherResponse.builder().message("TEST").build()));
		Pair<Long, WeatherResponse> value = cachedWeatherMap.get("test_url");
		assertNull(value);
	}

}

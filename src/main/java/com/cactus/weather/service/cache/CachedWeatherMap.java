package com.cactus.weather.service.cache;

import com.cactus.weather.service.model.response_model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.cactus.weather.service.util.OpenWeatherMapApiUtils.CACHE_TTL_IN_MINUTES;

@Slf4j
public class CachedWeatherMap extends ConcurrentHashMap<String, Pair<Long, WeatherResponse>>
		implements CachedWeatherMapInterface {

	// Calculate the TTL for the weather data and put it in the cache;
	// The TTL is calculated in milliseconds based on the generated time of the weather
	// data + 10 minutes (Refresh data time specified by OpenWeatherMapAPI Docs);
	public void cacheWeatherData(String currentWeatherQueryUrl, WeatherResponse weatherResponse) {
		long ttl = new Date(weatherResponse.getCurrentWeather().getDt() * 1000).getTime()
				+ TimeUnit.MINUTES.toMillis(CACHE_TTL_IN_MINUTES);
		put(currentWeatherQueryUrl, Pair.of(ttl, weatherResponse));
	}

	@Override
	public Pair<Long, WeatherResponse> put(String currentWeatherQueryUrl, Pair<Long, WeatherResponse> pair) {
		return super.put(currentWeatherQueryUrl, pair);
	}

	// First check if the currentWeatherQueryUrl is present and the TTL has not expired;
	// If the TTL has expired, remove the pairFromCache from the cache and return null;
	@Override
	public Pair<Long, WeatherResponse> get(Object currentWeatherQueryUrl) {
		Pair<Long, WeatherResponse> pairFromCache = super.get(currentWeatherQueryUrl);
		if (pairFromCache != null && pairFromCache.getKey() > System.currentTimeMillis()) {
			log.debug("Using cached pairFromCache for URL: {}", currentWeatherQueryUrl);
			return pairFromCache;
		}
		else {
			// Remove the expired pairFromCache from the cache
			log.debug("TTL Expired For Weather Data; Hence removing from cache for URL: {}", currentWeatherQueryUrl);
			remove(currentWeatherQueryUrl);
			return null;
		}
	}

}

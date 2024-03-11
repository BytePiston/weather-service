package com.verdant.weather.service.util;

public final class OpenWeatherMapApiUtils {

	// To use the class as a utility class and prevent instantiation;
	private OpenWeatherMapApiUtils() {
	}

	// Cache TTL in minutes; Can be moved to config-server when we scale the application;
	public static final int CACHE_TTL_IN_MINUTES = 10;

	public static final String STATUS_SUCCESS = "SUCCESS";

	// Endpoints for the OpenWeatherMap API;
	public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

	public static final String CURRENT_WEATHER_URL = BASE_URL + "weather";

	public static final String FORECAST_WEATHER_URL = BASE_URL + "forecast";

	// Response formats;
	public static final String RESPONSE_MODE_JSON = "&mode=json";

	public static final String RESPONSE_MODE_XML = "&mode=xml";

	// Units of measurement;
	public static final String QUERY_PARAM_UNITS = "&units=";

	public static final String CONSTANT_UNIT_METRIC = "metric";

	public static final String CONSTANT_UNIT_IMPERIAL = "imperial";

	public static final String CONSTANT_UNIT_STANDARD = "standard";

	public static final String QUERY_PARAM_UNITS_METRIC = QUERY_PARAM_UNITS + CONSTANT_UNIT_METRIC;

	public static final String QUERY_PARAM_UNITS_IMPERIAL = QUERY_PARAM_UNITS + CONSTANT_UNIT_IMPERIAL;

	public static final String QUERY_PARAM_UNITS_STANDARD = QUERY_PARAM_UNITS + CONSTANT_UNIT_STANDARD;

	// Query parameters placeholder for the OpenWeatherMap API calls;
	public static final String QUERY_PLACEHOLDER_CITY_NAME = "{city_name}";

	public static final String QUERY_PLACEHOLDER_STATE_CODE = "{state_code}";

	public static final String QUERY_PLACEHOLDER_COUNTRY_CODE = "{country_code}";

	public static final String QUERY_PLACEHOLDER_ZIP_CODE = "{zip_code}";

	public static final String QUERY_PLACEHOLDER_CITY_ID = "{city_id}";

	public static final String QUERY_PLACEHOLDER_LATITUDE = "{lat}";

	public static final String QUERY_PLACEHOLDER_LONGITUDE = "{lon}";

	public static final String QUERY_PLACEHOLDER_API_KEY = "{api_key}";

	// API key placeholder for the OpenWeatherMap API calls;
	private static final String QUERY_PARAM_API_KEY = "&appid=" + QUERY_PLACEHOLDER_API_KEY;

	// Query URL for the OpenWeatherMap API;
	public static final String QUERY_BY_COORDINATES = "?lat=" + QUERY_PLACEHOLDER_LATITUDE + "&lon="
			+ QUERY_PLACEHOLDER_LONGITUDE + QUERY_PARAM_API_KEY + RESPONSE_MODE_JSON + QUERY_PARAM_UNITS_METRIC;

	public static final String QUERY_BY_ZIP = "?zip=" + QUERY_PLACEHOLDER_ZIP_CODE + QUERY_PARAM_API_KEY
			+ RESPONSE_MODE_JSON + QUERY_PARAM_UNITS_METRIC;

	public static final String QUERY_BY_ZIP_COUNTRY = "?zip=" + QUERY_PLACEHOLDER_ZIP_CODE + ","
			+ QUERY_PLACEHOLDER_COUNTRY_CODE + QUERY_PARAM_API_KEY + RESPONSE_MODE_JSON + QUERY_PARAM_UNITS_METRIC;

	public static final String QUERY_BY_CITY_NAME = "?q=" + QUERY_PLACEHOLDER_CITY_NAME + QUERY_PARAM_API_KEY
			+ RESPONSE_MODE_JSON + QUERY_PARAM_UNITS_METRIC;

	public static final String QUERY_BY_CITY_COUNTRY = "?q=" + QUERY_PLACEHOLDER_CITY_NAME + ","
			+ QUERY_PLACEHOLDER_COUNTRY_CODE + QUERY_PARAM_API_KEY + RESPONSE_MODE_JSON + QUERY_PARAM_UNITS_METRIC;

	public static final String QUERY_BY_CITY_STATE_COUNTRY = "?q=" + QUERY_PLACEHOLDER_CITY_NAME + ","
			+ QUERY_PLACEHOLDER_STATE_CODE + "," + QUERY_PLACEHOLDER_COUNTRY_CODE + QUERY_PARAM_API_KEY
			+ RESPONSE_MODE_JSON + QUERY_PARAM_UNITS_METRIC;

	public static final String QUERY_BY_CITY_ID = "?id=" + QUERY_PLACEHOLDER_CITY_ID + QUERY_PARAM_API_KEY
			+ RESPONSE_MODE_JSON + QUERY_PARAM_UNITS_METRIC;

}

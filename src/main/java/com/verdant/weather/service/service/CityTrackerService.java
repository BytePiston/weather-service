package com.verdant.weather.service.service;

import com.verdant.weather.service.model.response_model.CityTrackerResponse;
import com.verdant.weather.service.util.CityTrackerMode;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CityTrackerService {

	List<CityTrackerResponse> getAllCityTracker(Pageable pageable);

	Optional<CityTrackerResponse> getCityTracker(String cityName);

	void incrementCityTracker(String cityName, CityTrackerMode mode);

}

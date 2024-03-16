package com.cactus.weather.service.service;

import com.cactus.weather.service.model.response_model.CityTrackerResponse;
import com.cactus.weather.service.util.CityTrackerMode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CityTrackerService {

  List<CityTrackerResponse> getAllCityTracker(Pageable pageable);

  Optional<CityTrackerResponse> getCityTracker(String cityName);

  void incrementCityTracker(String cityName, CityTrackerMode mode);
}

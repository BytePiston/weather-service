package com.cactus.weather.service.service.impl;

import com.cactus.weather.service.entity.CitySearchTracker;
import com.cactus.weather.service.model.response_model.CityTrackerResponse;
import com.cactus.weather.service.repository.CityTrackerRepository;
import com.cactus.weather.service.service.CityTrackerService;
import com.cactus.weather.service.util.CityTrackerMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CityTrackerServiceImpl implements CityTrackerService {

  CityTrackerRepository cityTrackerRepository;

  @Autowired
  public CityTrackerServiceImpl(CityTrackerRepository cityTrackerRepository) {
    this.cityTrackerRepository = cityTrackerRepository;
  }

  @Override
  public List<CityTrackerResponse> getAllCityTracker(Pageable pageable) {
    Page<CitySearchTracker> responses = cityTrackerRepository.findAll(pageable);
    if (responses.isEmpty()) {
      return new ArrayList<>();
    } else {
      List<CityTrackerResponse> cityTrackerResponses = new ArrayList<>();
      responses.forEach(
          citySearchTracker -> {
            cityTrackerResponses.add(
                CityTrackerResponse.builder()
                    .name(citySearchTracker.getCityName())
                    .idCount(citySearchTracker.getIdCount())
                    .nameCount(citySearchTracker.getNameCount())
                    .zipCodeCount(citySearchTracker.getZipCount())
                    .totalCounter(citySearchTracker.getTotalCount())
                    .build());
          });
      return cityTrackerResponses;
    }
  }

  @Override
  public Optional<CityTrackerResponse> getCityTracker(String cityName) {

    Optional<CitySearchTracker> optionalTracker = cityTrackerRepository.findById(cityName);
    if (optionalTracker.isPresent()) {
      CitySearchTracker citySearchTracker = optionalTracker.get();
      return Optional.of(
          CityTrackerResponse.builder()
              .name(citySearchTracker.getCityName())
              .idCount(citySearchTracker.getIdCount())
              .nameCount(citySearchTracker.getNameCount())
              .zipCodeCount(citySearchTracker.getZipCount())
              .totalCounter(citySearchTracker.getTotalCount())
              .build());
    }
    return Optional.empty();
  }

  @Override
  public void incrementCityTracker(String cityName, CityTrackerMode mode) {
    Optional<CitySearchTracker> optionalTracker = cityTrackerRepository.findById(cityName);
    CitySearchTracker citySearchTracker;
    if (optionalTracker.isPresent()) {
      citySearchTracker = optionalTracker.get();
    } else {
      citySearchTracker =
          CitySearchTracker.builder()
              .cityName(cityName)
              .nameCount(0L)
              .idCount(0L)
              .coordinateCount(0L)
              .zipCount(0L)
              .totalCount(0L)
              .build();
    }
    if (mode.equals(CityTrackerMode.ID)) {
      citySearchTracker.incrementIdCount();
    } else if (mode.equals(CityTrackerMode.COORDINATE)) {
      citySearchTracker.incrementCoordinateCount();
    } else if (mode.equals(CityTrackerMode.ZIP)) {
      citySearchTracker.incrementZipCount();
    } else {
      citySearchTracker.incrementNameCount();
    }
    cityTrackerRepository.save(citySearchTracker);
  }
}

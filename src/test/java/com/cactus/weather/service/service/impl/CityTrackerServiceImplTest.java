package com.cactus.weather.service.service.impl;

import com.cactus.weather.service.service.impl.CityTrackerServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.cactus.weather.service.entity.CitySearchTracker;
import com.cactus.weather.service.model.response_model.CityTrackerResponse;
import com.cactus.weather.service.repository.CityTrackerRepository;
import com.cactus.weather.service.util.CityTrackerMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
class CityTrackerServiceImplTest {

  @MockBean private CityTrackerRepository cityTrackerRepository;

  private CityTrackerServiceImpl cityTrackerService;

  private List<CitySearchTracker> allCityTrackerResponse;

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);
    cityTrackerService = new CityTrackerServiceImpl(cityTrackerRepository);
    allCityTrackerResponse = new ArrayList<>();
    ArrayNode jsonNode =
        (ArrayNode)
            objectMapper.readTree(
                new File("src/test/resources/weather_data/AllCityTrackerResponse.json"));
    for (JsonNode node : jsonNode) {
      CitySearchTracker citySearchTracker =
          CitySearchTracker.builder()
              .cityName(node.get("name").asText())
              .idCount(node.get("idCount").asLong())
              .nameCount(node.get("nameCount").asLong())
              .zipCount(node.get("zipCodeCount").asLong())
              .totalCount(node.get("totalCounter").asLong())
              .build();

      allCityTrackerResponse.add(citySearchTracker);
    }

    JsonNode node =
        objectMapper.readTree(
            new File("src/test/resources/weather_data/EdmontonCityTrackerResponse.json"));

    CitySearchTracker citySearchTracker =
        CitySearchTracker.builder()
            .cityName(node.get("name").asText())
            .idCount(node.get("idCount").asLong())
            .nameCount(node.get("nameCount").asLong())
            .zipCount(node.get("zipCodeCount").asLong())
            .totalCount(node.get("totalCounter").asLong())
            .build();

    Page<CitySearchTracker> page =
        convertArrayListToPage(allCityTrackerResponse, PageRequest.of(0, 3));
    when(cityTrackerRepository.findAll(any(Pageable.class))).thenReturn(page);
    when(cityTrackerRepository.findById(any())).thenReturn(Optional.of(citySearchTracker));
  }

  @Test
  @DisplayName("Positive Scenario: Get all city tracker")
  void getAllCityTrackerPositiveScenario() {
    cityTrackerService.getAllCityTracker(PageRequest.of(0, 10));
    when(cityTrackerRepository.findById(any()))
        .thenReturn(Optional.of(allCityTrackerResponse.get(0)));
    Page<CitySearchTracker> page = cityTrackerRepository.findAll(PageRequest.of(0, 3));
    List<CityTrackerResponse> cityTrackerResponses = new ArrayList<>();
    page.forEach(
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
    assertEquals(3, cityTrackerResponses.size());
  }

  @Test
  @DisplayName("Positive Scenario: Get All city tracker with 1 Page Size")
  void getAllCityTrackerPositiveScenarioWithOnePageSize() {
    Page<CitySearchTracker> page =
        convertArrayListToPage(allCityTrackerResponse, PageRequest.of(0, 1));
    when(cityTrackerRepository.findAll(any(Pageable.class))).thenReturn(page);
    List<CityTrackerResponse> cityTrackerResponses = new ArrayList<>();
    page.forEach(
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
    assertEquals(1, cityTrackerResponses.size());
  }

  @Test
  @DisplayName("Positive Scenario: Get City Tracker")
  void getCityTrackerPositiveScenario() {
    Optional<CityTrackerResponse> cityTrackerResponse =
        cityTrackerService.getCityTracker("Edmonton");
    assertTrue(cityTrackerResponse.isPresent());
    assertEquals(cityTrackerResponse.get().getName(), "Edmonton");
  }

  @Test
  @DisplayName("Negative Scenario: Get City Tracker")
  void getCityTrackerNegativeScenario() {
    when(cityTrackerRepository.findById(any())).thenReturn(Optional.empty());
    Optional<CityTrackerResponse> cityTrackerResponse =
        cityTrackerService.getCityTracker("Edmonton");
    assertTrue(cityTrackerResponse.isEmpty());
  }

  @Test
  @DisplayName("Positive Scenario: Increment City Tracker")
  void incrementCityTrackerPositiveScenario() {
    // Verify the initial count
    assertEquals(12, cityTrackerService.getCityTracker("Edmonton").get().getNameCount());
    assertEquals(16, cityTrackerService.getCityTracker("Edmonton").get().getTotalCounter());

    cityTrackerService.incrementCityTracker("Edmonton", CityTrackerMode.NAME);
    Optional<CityTrackerResponse> cityTrackerResponse =
        cityTrackerService.getCityTracker("Edmonton");

    // Verify the incremented count
    assertTrue(cityTrackerResponse.isPresent());
    assertEquals(13, cityTrackerResponse.get().getNameCount());
    assertEquals(17, cityTrackerResponse.get().getTotalCounter());
  }

  @Test
  @DisplayName("Negative Scenario: Increment City Tracker")
  void incrementCityTrackerNegativeScenario() {
    when(cityTrackerRepository.findById(any())).thenReturn(Optional.empty());
    cityTrackerService.incrementCityTracker("Edmonton", CityTrackerMode.NAME);
    Optional<CityTrackerResponse> cityTrackerResponse =
        cityTrackerService.getCityTracker("Edmonton");
    assertTrue(cityTrackerResponse.isEmpty());
  }

  @Test
  @DisplayName("Positive Scenario: Increment City Tracker with Zip Code")
  void incrementCityTrackerPositiveScenarioWithZipCode() {
    // Verify the initial count
    assertEquals(cityTrackerService.getCityTracker("Edmonton").get().getZipCodeCount(), 0);
    assertEquals(cityTrackerService.getCityTracker("Edmonton").get().getTotalCounter(), 16);

    cityTrackerService.incrementCityTracker("Edmonton", CityTrackerMode.ZIP);
    Optional<CityTrackerResponse> cityTrackerResponse =
        cityTrackerService.getCityTracker("Edmonton");

    // Verify the incremented count
    assertTrue(cityTrackerResponse.isPresent());
    assertEquals(1, cityTrackerResponse.get().getZipCodeCount());
    assertEquals(17, cityTrackerResponse.get().getTotalCounter());
  }

  @Test
  @DisplayName("Negative Scenario: Increment City Tracker with Zip Code")
  void incrementCityTrackerNegativeScenarioWithZipCode() {
    when(cityTrackerRepository.findById(any())).thenReturn(Optional.empty());
    cityTrackerService.incrementCityTracker("Edmonton", CityTrackerMode.ZIP);
    Optional<CityTrackerResponse> cityTrackerResponse =
        cityTrackerService.getCityTracker("Edmonton");
    assertTrue(cityTrackerResponse.isEmpty());
  }

  @Test
  @DisplayName("Negative Scenario: Get All City Tracker with Invalid Page Number and Page Size")
  void getAllCityTrackerNegativeScenarioWithInvalidPageInput() {
    assertThrows(
        IllegalArgumentException.class,
        () -> cityTrackerService.getAllCityTracker(PageRequest.of(-1, 10)));
    assertThrows(
        IllegalArgumentException.class,
        () -> cityTrackerService.getAllCityTracker(PageRequest.of(-1, -1)));
    assertThrows(
        IllegalArgumentException.class,
        () -> cityTrackerService.getAllCityTracker(PageRequest.of(0, 0)));
    assertThrows(
        IllegalArgumentException.class,
        () -> cityTrackerService.getAllCityTracker(PageRequest.of(0, -1)));
  }

  // Helper method to convert List to Page
  public static Page<CitySearchTracker> convertArrayListToPage(
      List<CitySearchTracker> list, Pageable pageable) {
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), list.size());
    List<CitySearchTracker> subList = list.subList(start, end);
    long totalElements = list.size();
    return new PageImpl<>(subList, pageable, totalElements);
  }
}

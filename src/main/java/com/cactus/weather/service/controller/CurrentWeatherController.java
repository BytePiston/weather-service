package com.cactus.weather.service.controller;

import com.cactus.weather.service.exception.ParameterValidationException;
import com.cactus.weather.service.exception.ResourceNotFoundException;
import com.cactus.weather.service.model.request_model.CityRequestModel;
import com.cactus.weather.service.model.request_model.CoordinateRequestModel;
import com.cactus.weather.service.model.request_model.ZipCodeRequestModel;
import com.cactus.weather.service.model.response_model.WeatherResponse;
import com.cactus.weather.service.service.WeatherService;
import com.cactus.weather.service.validator.RequestValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Current Weather APIs",
    description =
        "APIs to retrieve the current weather of the city based on provided parameters.")
@RestController
@RequestMapping("api/v1/current/weather")
public class CurrentWeatherController {

  private final WeatherService weatherService;

  @Autowired
  public CurrentWeatherController(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  /**
   * ***************************************************************************************************************************
   * API to retrieve the current weather of the city based on provided
   * parameters; Either city ID or city name is required; State code and country
   * code are optional;
   *
   * @param id - ID of the city; (Optional)
   * @param name - Name of the city; (Optional)
   * @param stateCode - State code of the city; (Optional)
   * @param countryCode - Country code of the city; (Optional)
   * @return WeatherResponse - Current weather of the city based on below
   *     scenarios; <p>If id is provided, the API will return the current
   *     weather of the city; GET URL: api/v1/current/weather/city?id={id};
   *     <p>Else, if city name is provided, the API will return the current
   * weather of the city based on below scenarios; <p>1) If state code and
   * country code are not provided, the API will return the current weather of
   * the city; GET URL: api/v1/current/weather/city?name={cityName}; <p>2) If
   * state code is provided, the API will return the current weather of the city
   * in the state; GET URL:
   * api/v1/current/weather/city?name={name}&stateCode={stateCode}; <p>3) If
   * state code and country code are provided, the API will return the current
   * weather of the city in the state of the country; GET URL:
   *     api/v1/current/weather/city?name={cityName}&stateCode={stateCode}&countryCode={countryCode};
   *     **************************************************************************************************************************
   */
  @GetMapping("/city")
  @Operation(
      summary = "Get current weather by city",
      description =
          "Get current weather by city ID or city name; State code and country code are optional")
  @ApiResponses({
    @ApiResponse(responseCode = "200",
                 content =
                 {
                   @Content(mediaType = "application/json",
                            schema =
                                @Schema(implementation = WeatherResponse.class))
                 })
    ,
        @ApiResponse(
            responseCode = "400",
            description =
                "Bad Request, Please verify the request parameters and try again",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description =
                "Weather information not found for specified parameters",
            content = @Content)
  })
  public ResponseEntity<WeatherResponse>
  getCurrentWeatherByCity(
      @Parameter(description = "ID of the City; Required Field")
      @RequestParam Optional<String> id,
      @Parameter(description = "Name of City; Required Field")
      @RequestParam Optional<String> name,
      @Parameter(description = "State Code; Optional Field")
      @RequestParam Optional<String> stateCode,
      @Parameter(description = "Country Code; Optional Field")
      @RequestParam Optional<String> countryCode)
      throws ResourceNotFoundException {
    List<String> errorMessages = new ArrayList<>();

    if (id.isEmpty() && name.isEmpty()) {
      return ResponseEntity.badRequest().body(
          WeatherResponse.builder()
              .message("Either City ID or City name is required")
              .build());
    }

    if (id.isPresent()) {
      try {
        RequestValidationUtil.validateCityId(id.get());
        Optional<WeatherResponse> weatherResponse =
            weatherService.getCurrentWeatherByCityId(id.get());
        return prepareWeatherResponse(weatherResponse);
      } catch (ParameterValidationException e) {
        errorMessages.add(e.getMessage());
      }
    } else {
      try {
        RequestValidationUtil.validateCityName(name.get());
        if (stateCode.isPresent() && countryCode.isPresent()) {
          RequestValidationUtil.validateStateCode(stateCode.get());
          RequestValidationUtil.validateCountryCode(countryCode.get());
          Optional<WeatherResponse> weatherResponse =
              weatherService
                  .getCurrentWeatherByCityNameAndStateCodeAndCountryCode(
                      name.get(), stateCode.get(), countryCode.get());
          return prepareWeatherResponse(weatherResponse);
        } else if (countryCode.isPresent()) {
          RequestValidationUtil.validateCountryCode(countryCode.get());
          Optional<WeatherResponse> weatherResponse =
              weatherService.getCurrentWeatherByCityNameAndCountryCode(
                  name.get(), countryCode.get());
          return prepareWeatherResponse(weatherResponse);
        } else {
          Optional<WeatherResponse> weatherResponse =
              weatherService.getCurrentWeatherByCityName(name.get());
          return prepareWeatherResponse(weatherResponse);
        }
      } catch (ParameterValidationException e) {
        errorMessages.add(e.getMessage());
      }
    }

    // If there are any validation errors, return bad request with error
    // messages;
    String errorMessage = String.join(", ", errorMessages);
    throw new ParameterValidationException(errorMessage);
  }

  /**
   * API to retrieve the current weather based on the zip code and country code;
   *
   * @param zipCode - Name of the city; (Required)
   * @param countryCode - Country code of the city; (Optional)
   * @return ResponseEntity<WeatherResponse> - Current weather of the city based
   *     on below scenarios; <p>1) If country code is not provided, the API will
   *     return the current weather based on zip code; GET URL:
   *     api/v1/current/weather/zip?zipCode={zipCode}; <p>2) If country code is
   *     provided, the API will return the current weather based on zip code and
   *     country code; GET URL:
   *     api/v1/current/weather/zip?zipCode={zipCode}&countryCode={countryCode};
   */
  @GetMapping("/zip")
  @Operation(
      summary = "Get current weather by zip",
      description = "Get current weather by zip code; Country code is optional")
  @ApiResponses({
    @ApiResponse(responseCode = "200",
                 content =
                 {
                   @Content(mediaType = "application/json",
                            schema =
                                @Schema(implementation = WeatherResponse.class))
                 })
    ,
        @ApiResponse(
            responseCode = "400",
            description =
                "Bad Request, Please verify the request parameters and try again",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description =
                "Weather information not found for specified parameters",
            content = @Content)
  })
  public ResponseEntity<WeatherResponse>
  getCurrentWeatherByZip(
      @Parameter(description = "Zip; Required Field",
                 required = true) @RequestParam String zipCode,
      @Parameter(description = "Country Code; Optional Field")
      @RequestParam Optional<String> countryCode)
      throws ResourceNotFoundException {
    List<String> errorMessages = new ArrayList<>();
    try {
      RequestValidationUtil.validateZipCode(zipCode);
    } catch (ParameterValidationException e) {
      errorMessages.add(e.getMessage());
    }
    Optional<WeatherResponse> weatherResponse;
    if (countryCode.isPresent() && errorMessages.isEmpty()) {
      try {
        RequestValidationUtil.validateCountryCode(countryCode.get());
        weatherResponse =
            weatherService.getCurrentWeatherByZipCodeAndCountryCode(
                zipCode, countryCode.get());
        return prepareWeatherResponse(weatherResponse);
      } catch (ParameterValidationException e) {
        errorMessages.add(e.getMessage());
      }
    } else if (errorMessages.isEmpty()) {
      weatherResponse = weatherService.getCurrentWeatherByZipCode(zipCode);
      return prepareWeatherResponse(weatherResponse);
    }

    // If there are any validation errors, return bad request with error
    // messages;
    String errorMessage = String.join(", ", errorMessages);
    throw new ParameterValidationException(errorMessage);
  }

  /**
   * API to retrieve the current weather based on the provided latitude and
   * longitude;
   *
   * <p>GET URL:
   * api/v1/current/weather/coordinates?latitude={latitude}&longitude={longitude};
   *
   * @param latitude - Latitude of the location; (Required)
   * @param longitude - Longitude of the location; (Required)
   * @return ResponseEntity<WeatherResponse> - Current weather of the location
   *     based on provided latitude and longitude;
   */
  @GetMapping("/coordinates")
  @Operation(
      summary = "Get current weather by coordinates",
      description =
          "Get current weather by latitude and longitude; Required Fields")
  @ApiResponses({
    @ApiResponse(responseCode = "200",
                 content =
                 {
                   @Content(mediaType = "application/json",
                            schema =
                                @Schema(implementation = WeatherResponse.class))
                 })
    ,
        @ApiResponse(
            responseCode = "400",
            description =
                "Bad Request, Please verify the request parameters and try again",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description =
                "Weather information not found for specified parameters",
            content = @Content)
  })
  public ResponseEntity<WeatherResponse>
  getCurrentWeatherByCoordinates(
      @Parameter(description = "Latitude; Required Field",
                 required = true) @RequestParam Double latitude,
      @Parameter(description = "Longitude; Required Field", required = true)
      @RequestParam Double longitude) throws ResourceNotFoundException {
    List<String> errorMessages = new ArrayList<>();
    try {
      RequestValidationUtil.validateCoordinate(latitude, longitude);
    } catch (ParameterValidationException e) {
      errorMessages.add(e.getMessage());
    }

    if (errorMessages.isEmpty()) {
      Optional<WeatherResponse> weatherResponse =
          weatherService.getCurrentWeatherByCoordinates(latitude, longitude);
      return prepareWeatherResponse(weatherResponse);
    }

    // If there are any validation errors, return bad request with error
    // messages;
    String errorMessage = String.join(", ", errorMessages);
    throw new ParameterValidationException(errorMessage);
  }

  /**
   * ***************************************************************************************************************************
   * Please Note: The below POST as GET methods are not required for the current
   * implementation; But can be used for future enhancements; The below methods
   * are added to demonstrate the usage of POST as GET method to retrieve the
   * current weather;
   * **************************************************************************************************************************
   */

  /**
   * POST as GET method to retrieve the current weather of the city based on
   * provided parameters as JSON request;
   *
   * <p>POST URL: api/v1/current/weather/city;
   *
   * @param cityModel - CityRequestModel - Request model for city name, state
   *     code and country code;
   * @return ResponseEntity<WeatherResponse> - Current weather of the city based
   *     on following scenarios; <p>1) If state code and country code are not
   *     provided, the API will return the current weather of the city; GET URL:
   *     api/v1/current/weather/city?name={cityName}; <p>2) If state code is
   *     provided, the API will return the current weather of the city in the
   *     state; GET URL:
   * api/v1/current/weather/city?name={cityName}&stateCode={stateCode}; <p>3) If
   * state code and country code are provided, the API will return the current
   * weather of the city in the state of the country; GET URL:
   *     api/v1/current/weather/city?name={cityName}&stateCode={stateCode}&countryCode={countryCode};
   */
  @PostMapping("/city")
  @Operation(
      summary = "POST as GET: Get current weather by city request model",
      description =
          "POST as GET: Get current weather by city name, state code and country code; State code and country code are optional")
  @ApiResponses({
    @ApiResponse(responseCode = "200",
                 content =
                 {
                   @Content(mediaType = "application/json",
                            schema =
                                @Schema(implementation = WeatherResponse.class))
                 })
    ,
        @ApiResponse(
            responseCode = "400",
            description =
                "Bad Request, Please verify the request parameters and try again",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description =
                "Weather information not available for specified parameters",
            content = @Content)
  })
  public ResponseEntity<WeatherResponse>
  getCurrentWeatherByCityRequestModel(@Valid
                                      @RequestBody CityRequestModel cityModel)
      throws ResourceNotFoundException {
    Optional<WeatherResponse> weatherResponse;
    if (cityModel.hasId()) {
      weatherResponse =
          weatherService.getCurrentWeatherByCityId(cityModel.getId());
    } else if (cityModel.hasName() && cityModel.getCountryCode() != null &&
               cityModel.getStateCode() != null) {
      weatherResponse =
          weatherService.getCurrentWeatherByCityNameAndStateCodeAndCountryCode(
              cityModel.getName(), cityModel.getStateCode(),
              cityModel.getCountryCode());
    } else if (cityModel.hasName() && cityModel.getCountryCode() != null) {
      weatherResponse =
          weatherService.getCurrentWeatherByCityNameAndCountryCode(
              cityModel.getName(), cityModel.getCountryCode());
    } else {
      weatherResponse =
          weatherService.getCurrentWeatherByCityName(cityModel.getName());
    }
    return prepareWeatherResponse(weatherResponse);
  }

  /**
   * POST as GET method to retrieve the current weather of the location based on
   * provided parameters as JSON request;
   *
   * <p>POST URL: api/v1/current/weather/zip;
   *
   * @param zipCodeModel - Request model for zip code and country code;
   * @return ResponseEntity<WeatherResponse> - Current weather of the location
   *     based on following scenarios; <p>1) If state code and country code are
   *     not provided, the API will return the current weather of the city; GET
   *     URL: api/v1/current/weather/city?name={cityName}; <p>2) If state code
   *     is provided, the API will return the current weather of the city in the
   *     state; GET URL:
   * api/v1/current/weather/city?name={cityName}&stateCode={stateCode}; <p>3) If
   * state code and country code are provided, the API will return the current
   * weather of the city in the state of the country; GET URL:
   *     api/v1/current/weather/city?name={cityName}&stateCode={stateCode}&countryCode={countryCode};
   */
  @PostMapping("/zip")
  @Operation(
      summary = "POST as GET: Get current weather by zip request model",
      description =
          "POST as GET: Get current weather by zip code and country code; Country code is optional")
  @ApiResponses({
    @ApiResponse(responseCode = "200",
                 content =
                 {
                   @Content(mediaType = "application/json",
                            schema =
                                @Schema(implementation = WeatherResponse.class))
                 })
    ,
        @ApiResponse(
            responseCode = "400",
            description =
                "Bad Request, Please verify the request parameters and try again",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description =
                "Weather information not available for specified parameters",
            content = @Content)
  })
  public ResponseEntity<WeatherResponse>
  getCurrentWeatherByZipRequestModel(
      @Valid @RequestBody ZipCodeRequestModel zipCodeModel)
      throws ResourceNotFoundException {
    Optional<WeatherResponse> weatherResponse;
    if (zipCodeModel.getZipCode() != null &&
        zipCodeModel.getCountryCode() != null) {
      weatherResponse = weatherService.getCurrentWeatherByZipCodeAndCountryCode(
          zipCodeModel.getZipCode(), zipCodeModel.getCountryCode());
    } else {
      weatherResponse =
          weatherService.getCurrentWeatherByZipCode(zipCodeModel.getZipCode());
    }
    return prepareWeatherResponse(weatherResponse);
  }

  /**
   * POST as GET method to retrieve the current weather of the location based on
   * provided parameters as JSON request;
   *
   * <p>POST URL: api/v1/current/weather/coordinates;
   *
   * @param coordinateModel - Request model for latitude and longitude;
   * @return ResponseEntity<WeatherResponse> - Current weather of the location
   *     based on provided latitude and longitude;
   */
  @PostMapping("/coordinates")
  @Operation(
      summary = "POST as GET: Get current weather by coordinates request model",
      description =
          "POST as GET: Get current weather by coordinates; Latitude and Longitude are required fields")
  @ApiResponses({
    @ApiResponse(responseCode = "200",
                 content =
                 {
                   @Content(mediaType = "application/json",
                            schema =
                                @Schema(implementation = WeatherResponse.class))
                 })
    ,
        @ApiResponse(
            responseCode = "400",
            description =
                "Bad Request, Please verify the request parameters and try again",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description =
                "Weather information not available for specified parameters",
            content = @Content)
  })
  public ResponseEntity<WeatherResponse>
  getCurrentWeatherByCoordinateRequestModel(
      @Valid @RequestBody CoordinateRequestModel coordinateModel)
      throws ResourceNotFoundException {
    Optional<WeatherResponse> weatherResponse =
        weatherService.getCurrentWeatherByCoordinates(
            coordinateModel.getLatitude(), coordinateModel.getLongitude());
    return prepareWeatherResponse(weatherResponse);
  }

  // Helper method to prepare the weather response;
  private ResponseEntity<WeatherResponse>
  prepareWeatherResponse(Optional<WeatherResponse> weatherResponse)
      throws ResourceNotFoundException {
    if (weatherResponse.isPresent()) {
      WeatherResponse response = weatherResponse.get();
      return ResponseEntity.ok(response);
    }
    throw new ResourceNotFoundException(
        "Weather information not available for specified parameters.", null,
        false, false);
  }
}

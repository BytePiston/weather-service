package com.cactus.weather.service.model.request_model;

import com.cactus.weather.service.validator.EitherIdOrNamePresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EitherIdOrNamePresent
public class CityRequestModel {

  @Pattern(regexp = "^\\d+$", message = "City ID must contain only digits")
  private String id;

  @Pattern(regexp = "^[a-zA-Z]+$", message = "City name must contain only letters and spaces")
  private String name;

  @Pattern(regexp = "^[a-zA-Z]{2}$", message = "State code must be a two-letter code")
  private String stateCode;

  @Pattern(regexp = "^[a-zA-Z]{2}$", message = "Country code must be a two-letter code")
  private String countryCode;

  public boolean hasId() {
    return Objects.nonNull(id) && !id.isEmpty();
  }

  public boolean hasName() {
    return Objects.nonNull(name) && !name.isEmpty();
  }
}

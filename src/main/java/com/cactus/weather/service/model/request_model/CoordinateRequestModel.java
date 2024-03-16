package com.cactus.weather.service.model.request_model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateRequestModel {

  @NotNull(message = "Latitude cannot be null")
  @DecimalMin(value = "-90", message = "Latitude must be between -90 and 90")
  @DecimalMax(value = "90", message = "Latitude must be between -90 and 90")
  private Double latitude;

  @NotNull(message = "Longitude cannot be null")
  @DecimalMin(value = "-180", message = "Longitude must be between -180 and 180")
  @DecimalMax(value = "180", message = "Longitude must be between -180 and 180")
  private Double longitude;
}

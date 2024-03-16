package com.cactus.weather.service.model.response_model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityTrackerResponse {

  String name;

  Long idCount;

  Long coordinateCount;

  Long nameCount;

  Long zipCodeCount;

  Long totalCounter;

  String message;
}

package com.cactus.weather.service.event;

import com.cactus.weather.service.util.CityTrackerMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CityTrackerEvent extends ApplicationEvent {

  private final String cityName;

  private final CityTrackerMode mode;

  public CityTrackerEvent(String cityName, CityTrackerMode mode) {
    super(cityName);
    this.cityName = cityName;
    this.mode = mode;
  }
}

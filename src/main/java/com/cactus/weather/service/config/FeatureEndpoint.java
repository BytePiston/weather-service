package com.cactus.weather.service.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Endpoint(id = "feature")
public class FeatureEndpoint {

  private final Map<String, Feature> featureMap = new ConcurrentHashMap<>();

  public FeatureEndpoint() {
    featureMap.put("City Name", new Feature(true));
    featureMap.put("Zip Code", new Feature(true));
    featureMap.put("Coordinate", new Feature(true));
    featureMap.put("City Id", new Feature(true));
    featureMap.put("City Search Tracker", new Feature(true));
  }

  @ReadOperation
  public Map<String, Feature> features() {
    return featureMap;
  }

  @ReadOperation
  public Feature feature(@Selector String featureName) {
    return featureMap.get(featureName);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Feature {

    private boolean isEnabled;
  }
}

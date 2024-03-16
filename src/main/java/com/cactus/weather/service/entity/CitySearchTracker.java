package com.cactus.weather.service.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitySearchTracker implements Serializable {

  @Id @Column(nullable = false) private String cityName;

  private Long nameCount;

  private Long idCount;

  private Long coordinateCount;

  private Long zipCount;

  private Long totalCount;

  public void incrementIdCount() {
    this.idCount++;
    this.totalCount++;
  }

  public void incrementNameCount() {
    this.nameCount++;
    this.totalCount++;
  }

  public void incrementZipCount() {
    this.zipCount++;
    this.totalCount++;
  }

  public void incrementCoordinateCount() {
    this.coordinateCount++;
    this.totalCount++;
  }
}

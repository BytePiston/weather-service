package com.cactus.weather.service.repository;

import com.cactus.weather.service.entity.CitySearchTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityTrackerRepository extends JpaRepository<CitySearchTracker, String> {

  /**
   * *********************************************************************************************
   * Future Use Cases of CitySearchTrackerRepository;
   *
   * <p>Please Note that these are not implemented in the current version of the application. As
   * part of the future enhancements, the following use cases can be implemented in the Controller
   * and Service classes.
   *
   * <p>1. Find the CitySearchTracker entity with the maximum total count. 2. Find the
   * CitySearchTracker entity with the minimum total count. 3. Find the number of CitySearchTracker
   * entities with a total count greater than or equal to the given value. 4. Find the number of
   * CitySearchTracker entities with a total count less than or equal to the given value.
   * *********************************************************************************************
   */

  /**
   * Finds the CitySearchTracker entity with the maximum total count.
   *
   * @return the CitySearchTracker entity with the maximum total count
   */
  CitySearchTracker findTopByOrderByTotalCountDesc();

  /**
   * Finds the CitySearchTracker entity with the minimum total count.
   *
   * @return the CitySearchTracker entity with the minimum total count
   */
  CitySearchTracker findFirstByOrderByTotalCountAsc();

  /**
   * Finds the number of CitySearchTracker entities with a total count greater than or equal to the
   * given value.
   *
   * @param totalCount the total count value
   * @return the number of CitySearchTracker entities with a total count greater than or equal to
   *     the given value
   */
  Long countByTotalCountGreaterThanEqual(Long totalCount);

  /**
   * Finds the number of CitySearchTracker entities with a total count less than or equal to the
   * given value.
   *
   * @param totalCount the total count value
   * @return the number of CitySearchTracker entities with a total count less than or equal to the
   *     given value
   */
  Long countByTotalCountLessThanEqual(Long totalCount);
}

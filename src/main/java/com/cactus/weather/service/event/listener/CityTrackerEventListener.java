package com.cactus.weather.service.event.listener;

import com.cactus.weather.service.event.CityTrackerEvent;
import com.cactus.weather.service.service.CityTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CityTrackerEventListener implements ApplicationListener<CityTrackerEvent> {

	private final CityTrackerService cityTrackerService;

	@Autowired
	public CityTrackerEventListener(CityTrackerService cityTrackerService) {
		this.cityTrackerService = cityTrackerService;
	}

	@Override
	public void onApplicationEvent(CityTrackerEvent event) {
		log.debug("Received city tracker event for city: {} and mode: {}", event.getCityName(), event.getMode());
		cityTrackerService.incrementCityTracker(event.getCityName(), event.getMode());
	}

}

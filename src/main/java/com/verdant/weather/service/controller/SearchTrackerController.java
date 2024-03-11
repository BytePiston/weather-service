package com.verdant.weather.service.controller;

import com.verdant.weather.service.exception.ParameterValidationException;
import com.verdant.weather.service.model.response_model.CityTrackerResponse;
import com.verdant.weather.service.service.CityTrackerService;
import com.verdant.weather.service.validator.RequestValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/tracker")
public class SearchTrackerController {

	private final CityTrackerService cityTrackerService;

	@Autowired
	public SearchTrackerController(CityTrackerService cityTrackerService) {
		this.cityTrackerService = cityTrackerService;
	}

	/**
	 * Get Analytical Data for City Search Tracker; Where City is tracked based on the
	 * number of times it is searched for by id, name, zip and coordinates; If City Name
	 * is provided, then it will return the analytical data for that city; Else, it will
	 * return the analytical data for all the cities;
	 * <p>
	 * Without Pagination: If Page Number and Page Size are not provided, then it will
	 * return the analytical data for all the cities;
	 * <p>
	 * GET: /api/v1/tracker/city -> Get All City Search Tracker
	 * <p>
	 * GET: /api/v1/tracker/city?page=0&pageSize=10 -> Get City Search Tracker with
	 * Pagination
	 * <p>
	 * With Pagination: If Page Number and Page Size are provided, then it will return the
	 * analytical data for the given page number and page size;
	 * <p>
	 * GET: /api/v1/tracker/city?name=cityName -> Get City Search Tracker for the given
	 * City Name GET: /api/v1/tracker/city?name=cityName&page=0&pageSize=10 -> Get City
	 * Search Tracker for the given City Name with Pagination
	 * <p>
	 * @param name - City Name; Required Field
	 * @param page - Page Number; Optional Field
	 * @param pageSize - Number of Entries per Page; Optional Field
	 * @return List of CityTrackerResponse - Analytical Data for City Search Tracker based
	 * on the search criteria; Also includes the error message if any;
	 */

	@GetMapping("/city")
	@Operation(summary = "Get City Search Tracker; If City Name Is Not Provided All Cities Tracker Will Be Returned",
			description = "Get City Search Tracker; If City Name Is Not Provided All Cities Tracker Will Be Returned")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CityTrackerResponse.class)) })
	public ResponseEntity<List<CityTrackerResponse>> getCityTracker(
			@Parameter(description = "City Name; Optional Field") @RequestParam Optional<String> name,
			@Parameter(description = "Page Number; Optional Field") @RequestParam(defaultValue = "0",
					required = false) Integer page,
			@Parameter(description = "Page Size: No of Entries per Page; Optional Field") @RequestParam(
					defaultValue = "10", required = false) Integer pageSize) {

		if (name.isPresent()) {
			List<String> errorMessages = new ArrayList<>();
			try {
				RequestValidationUtil.validateCityName(name.get());
				RequestValidationUtil.validatePageNumber(page);
				RequestValidationUtil.validatePageSize(pageSize);
			}
			catch (ParameterValidationException e) {
				errorMessages.add(e.getMessage());
			}
			if (!errorMessages.isEmpty()) {
				String errorMessage = String.join(", ", errorMessages);
				return ResponseEntity.badRequest()
					.body(List.of(CityTrackerResponse.builder().message(errorMessage).build()));
			}
			Optional<CityTrackerResponse> response = cityTrackerService.getCityTracker(name.get());
			if (response.isEmpty()) {
				return new ResponseEntity<>(List.of(CityTrackerResponse.builder().message("City Not Found").build()),
						HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(List.of(response.get()), HttpStatus.OK);
		}
		else {
			Pageable paging = PageRequest.of(page, pageSize);
			List<CityTrackerResponse> response = cityTrackerService.getAllCityTracker(paging);
			if (response.isEmpty()) {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

}

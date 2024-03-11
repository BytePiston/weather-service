package com.verdant.weather.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.verdant.weather.service.exception.ParameterValidationException;
import com.verdant.weather.service.model.response_model.CityTrackerResponse;
import com.verdant.weather.service.service.CityTrackerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchTrackerController.class)
class SearchTrackerControllerTest {

	@MockBean
	private CityTrackerService cityTrackerService;

	@Autowired
	private MockMvc mockMvc;

	private static List<CityTrackerResponse> allCityTrackerResponse;

	private static Optional<CityTrackerResponse> edmontonCityTrackerResponse;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeAll
	public static void setUp() throws IOException {
		allCityTrackerResponse = objectMapper.readValue(
				new File("src/test/resources/weather_data/AllCityTrackerResponse.json"),
				objectMapper.getTypeFactory().constructCollectionType(List.class, CityTrackerResponse.class));

		edmontonCityTrackerResponse = Optional
			.of(objectMapper.readValue(new File("src/test/resources/weather_data/EdmontonCityTrackerResponse.json"),
					CityTrackerResponse.class));
	}

	@Test
    @DisplayName("Positive Scenario: Get All City Tracker without Pagination; Exception Handling")
    void getAllCityTrackerWithoutPageSizeExceptionHandlingPositive() throws Exception {
        when(cityTrackerService.getAllCityTracker(PageRequest.of(0, 5))).thenThrow(ParameterValidationException.class);
        //Store returned value from mockmvc and parse the result;
        mockMvc.perform(get("/api/v1/tracker/city")).andExpect(status().isOk());
    }

	@Test
    @DisplayName("Positive Scenario: Get all city search tracker by page number and page size")
    void getAllCityTrackerWithPageSizePositive() throws Exception {
        when(cityTrackerService.getAllCityTracker(PageRequest.of(0, 3))).thenReturn(allCityTrackerResponse);
        //Store returned value from mockmvc and parse the result;
        ResultActions resultActions = mockMvc.perform(get("/api/v1/tracker/city?page=0&pageSize=3"));
        MvcResult result = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
        String content = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);
        assertTrue(node.isArray());
        assertTrue(node.size() == 3);
        assertTrue(node.get(0).get("name").asText().equals("Edmonton"));
        assertTrue(node.get(1).get("name").asText().equals("Hudson"));
        assertTrue(node.get(2).get("name").asText().equals("Leitchfield"));
    }

	@Test
    @DisplayName("Positive Scenario: Get all city search tracker by city name")
    void getAllCityTrackerByCityNamePositive() throws Exception {
        when(cityTrackerService.getCityTracker("Edmonton")).thenReturn(edmontonCityTrackerResponse);
        //Store returned value from mockmvc and parse the result;
        ResultActions resultActions = mockMvc.perform(get("/api/v1/tracker/city?name=Edmonton&page=0&pageSize=3"));
        MvcResult result = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
        String content = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);
        assertTrue(node.isArray());
        assertTrue(node.size() == 1);
        assertTrue(node.get(0).get("name").asText().equals("Edmonton"));
    }

	@Test
    @DisplayName("Negative Scenario: Get city search tracker by city name")
    void getAllCityTrackerByCityNameNegative() throws Exception {
        when(cityTrackerService.getCityTracker("Edmonton")).thenReturn(Optional.empty());
        //Store returned value from mockmvc and parse the result;
        ResultActions resultActions = mockMvc.perform(get("/api/v1/tracker/city?name=Edmonton&page=0&pageSize=3"));
        MvcResult result = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
        String content = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);
        assertTrue(node.isArray());
        assertTrue(node.size() == 1);
        assertEquals("City Not Found", node.get(0).get("message").asText());
    }

	@Test
    @DisplayName("Negative Scenario: Get all city search tracker by page number and page size")
    void getAllCityTrackerWithPageSizeNegative() throws Exception {
        when(cityTrackerService.getAllCityTracker(PageRequest.of(0, 3))).thenReturn(new ArrayList<>());
        //Store returned value from mockmvc and parse the result;
        ResultActions resultActions = mockMvc.perform(get("/api/v1/tracker/city?page=0&pageSize=3"));
        MvcResult result = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
        String content = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);
        assertTrue(node.isArray());
        assertTrue(node.size() == 0);
    }

}

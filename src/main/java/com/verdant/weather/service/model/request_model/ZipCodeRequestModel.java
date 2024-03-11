package com.verdant.weather.service.model.request_model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZipCodeRequestModel {

	@NotBlank(message = "Zip code cannot be blank")
	@Pattern(regexp = "^[a-zA-Z0-9]{1,10}$", message = "Invalid Zip code format")
	private String zipCode;

	@Pattern(regexp = "^[a-zA-Z]{2}$", message = "Country code must be a two-letter code")
	private String countryCode;

}

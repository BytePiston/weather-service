package com.verdant.weather.service.validator;

import com.verdant.weather.service.model.request_model.CityRequestModel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EitherIdOrNamePresentValidator implements ConstraintValidator<EitherIdOrNamePresent, CityRequestModel> {

	@Override
	public void initialize(EitherIdOrNamePresent constraintAnnotation) {
	}

	@Override
	public boolean isValid(CityRequestModel cityRequestModel, ConstraintValidatorContext context) {
		return cityRequestModel.getId() != null || cityRequestModel.getName() != null;
	}

}

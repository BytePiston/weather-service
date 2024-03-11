package com.verdant.weather.service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EitherIdOrNamePresentValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EitherIdOrNamePresent {

	String message() default "City ID or City name is required";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}

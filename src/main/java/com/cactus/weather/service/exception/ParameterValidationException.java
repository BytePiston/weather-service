package com.cactus.weather.service.exception;

public class ParameterValidationException extends IllegalArgumentException {

  public ParameterValidationException() { super(); }

  public ParameterValidationException(String message) { super(message); }

  public ParameterValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ParameterValidationException(Throwable cause) { super(cause); }
}

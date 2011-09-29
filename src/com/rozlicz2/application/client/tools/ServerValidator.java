package com.rozlicz2.application.client.tools;

import java.util.Set;

import javax.validation.ConstraintViolation;

public interface ServerValidator {

	Set<ConstraintViolation<?>> toClientValidations(Set<ConstraintViolation<?>> violations);

}
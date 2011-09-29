package com.rozlicz2.application.client.tools;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import com.google.gwt.validation.client.BaseMessageInterpolator.ContextImpl;
import com.google.gwt.validation.client.impl.ConstraintViolationImpl;
import com.google.inject.Inject;

public class ServerValidatorImpl implements ServerValidator {

	private ValidatorFactory factory;

	public ServerValidatorImpl() {
	}

	@Inject
	public void setValidatorFactory(ValidatorFactory validatorFactory) {
		factory = validatorFactory;
	}

	@Override
	public Set<ConstraintViolation<?>> toClientValidations(
			Set<ConstraintViolation<?>> violations) {
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<?>> newViolations = new HashSet<ConstraintViolation<?>>();
		for (ConstraintViolation<?> constraintViolation : violations) {
			@SuppressWarnings("unchecked")
			Class<Object> rootBeanClass = (Class<Object>) constraintViolation
					.getRootBeanClass();
			/*
			 * TODO (Jacek Marchwicki) - getConstrainsForClass should take
			 * entity object instead of Proxy element. Probably it can be done
			 * via @ProxyFor annotations
			 */
			BeanDescriptor constraintsForClass = validator
					.getConstraintsForClass(rootBeanClass);
			Path propertyPath = constraintViolation.getPropertyPath();
			String propertyName = propertyPath.toString();
			PropertyDescriptor constraintsForProperty = constraintsForClass
					.getConstraintsForProperty(propertyName);
			Set<ConstraintDescriptor<?>> constraintDescriptors = constraintsForProperty
					.getConstraintDescriptors();
			String messageTemplate = constraintViolation.getMessageTemplate();
			ConstraintDescriptor<?> constraintDescriptor = null;
			for (ConstraintDescriptor<?> constraintDescriptorLoop : constraintDescriptors) {
				String attribute = (String) constraintDescriptorLoop
						.getAttributes().get("message");
				if (attribute.equals(messageTemplate)) {
					constraintDescriptor = constraintDescriptorLoop;
					break;
				}
			}
			if (constraintDescriptor == null)
				continue;
			Object rootBean = constraintViolation.getRootBean();
			ContextImpl context = new ContextImpl(constraintDescriptor,
					rootBean);
			String interpolatedMessage = factory.getMessageInterpolator()
					.interpolate(messageTemplate, context);
			Object invalidValue = constraintViolation.getInvalidValue();
			;
			Object leafBean = constraintViolation.getLeafBean();
			ConstraintViolation<?> newConstraintViolation = ConstraintViolationImpl
					.builder().setConstraintDescriptor(constraintDescriptor)
					.setInvalidValue(invalidValue).setLeafBean(leafBean)
					.setMessage(interpolatedMessage)
					.setMessageTemplate(messageTemplate)
					.setPropertyPath(propertyPath).setRootBean(rootBean)
					.setRootBeanClass(rootBeanClass).build();
			newViolations.add(newConstraintViolation);
		}
		return newViolations;
	}
}

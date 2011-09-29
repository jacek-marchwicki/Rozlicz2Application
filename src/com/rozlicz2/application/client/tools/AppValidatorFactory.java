package com.rozlicz2.application.client.tools;

import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import com.rozlicz2.application.shared.entity.Project;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;
import com.rozlicz2.application.shared.proxy.ProjectListProxy;
import com.rozlicz2.application.shared.proxy.ProjectProxy;

public class AppValidatorFactory extends AbstractGwtValidatorFactory {

	@GwtValidation(value = { ProjectProxy.class, ExpenseProxy.class,
			ProjectListProxy.class, Project.class }, groups = { Default.class })
	public interface GwtValidator extends Validator {
	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}

}

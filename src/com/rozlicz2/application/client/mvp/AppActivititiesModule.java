package com.rozlicz2.application.client.mvp;

import com.google.gwt.inject.client.AbstractGinModule;
import com.rozlicz2.application.client.activity.AddParticipantActivity;
import com.rozlicz2.application.client.activity.ErrorActivity;
import com.rozlicz2.application.client.activity.ExpenseActivity;
import com.rozlicz2.application.client.activity.NotFoundActivity;
import com.rozlicz2.application.client.activity.ProjectActivity;
import com.rozlicz2.application.client.activity.ProjectsActivity;

public class AppActivititiesModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AddParticipantActivity.class);
		bind(ExpenseActivity.class);
		bind(NotFoundActivity.class);
		bind(ProjectActivity.class);
		bind(ProjectsActivity.class);
		bind(ErrorActivity.class);
	}

}

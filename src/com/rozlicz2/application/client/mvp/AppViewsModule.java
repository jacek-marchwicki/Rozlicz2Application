package com.rozlicz2.application.client.mvp;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.rozlicz2.application.client.Dashboard;
import com.rozlicz2.application.client.activity.AddParticipantActivity;
import com.rozlicz2.application.client.activity.ErrorActivity;
import com.rozlicz2.application.client.activity.ExpenseActivity;
import com.rozlicz2.application.client.activity.NotFoundActivity;
import com.rozlicz2.application.client.activity.ProjectActivity;
import com.rozlicz2.application.client.activity.ProjectsActivity;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.client.view.AddParticipantViewImpl;
import com.rozlicz2.application.client.view.ErrorView;
import com.rozlicz2.application.client.view.ErrorViewImpl;
import com.rozlicz2.application.client.view.ExpenseView;
import com.rozlicz2.application.client.view.ExpenseViewImpl;
import com.rozlicz2.application.client.view.NotFoundView;
import com.rozlicz2.application.client.view.NotFoundViewImpl;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.client.view.ProjectViewImpl;
import com.rozlicz2.application.client.view.ProjectsView;
import com.rozlicz2.application.client.view.ProjectsViewImpl;

public class AppViewsModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AddParticipantView.class).to(AddParticipantViewImpl.class).in(
				Singleton.class);
		bind(ExpenseView.class).to(ExpenseViewImpl.class).in(Singleton.class);
		bind(NotFoundView.class).to(NotFoundViewImpl.class).in(Singleton.class);
		bind(ProjectView.class).to(ProjectViewImpl.class).in(Singleton.class);
		bind(ProjectsView.class).to(ProjectsViewImpl.class).in(Singleton.class);
		bind(ErrorView.class).to(ErrorViewImpl.class).in(Singleton.class);
		bind(Dashboard.class).in(Singleton.class);

		bind(AddParticipantActivity.class);
		bind(ExpenseActivity.class);
		bind(NotFoundActivity.class);
		bind(ProjectActivity.class);
		bind(ProjectsActivity.class);
		bind(ErrorActivity.class);
	}

}

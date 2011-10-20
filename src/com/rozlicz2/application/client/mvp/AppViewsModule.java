package com.rozlicz2.application.client.mvp;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.client.view.AddParticipantViewImpl;
import com.rozlicz2.application.client.view.BreadcrumbView;
import com.rozlicz2.application.client.view.BreadcrumbViewImpl;
import com.rozlicz2.application.client.view.DashboardView;
import com.rozlicz2.application.client.view.DashboardViewImpl;
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
		bind(DashboardView.class).to(DashboardViewImpl.class).in(
				Singleton.class);
		bind(BreadcrumbView.class).to(BreadcrumbViewImpl.class).in(
				Singleton.class);
	}
}

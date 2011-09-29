package com.rozlicz2.application.client.mvp;

import javax.validation.ValidatorFactory;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.rozlicz2.application.client.tools.AppValidatorFactory;
import com.rozlicz2.application.client.tools.ServerValidator;
import com.rozlicz2.application.client.tools.ServerValidatorImpl;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;

public class AppModule extends AbstractGinModule {

	@Override
	protected void configure() {
		/**
		 * by GWT create -ListwidgetRequestFactory
		 * 
		 */
		bind(PlaceController.class).to(InjectablePlaceController.class).in(
				Singleton.class);
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(PopupActivityMapper.class).in(Singleton.class);
		bind(AppActivityMapper.class).in(Singleton.class);
		bind(ListwidgetRequestFactory.class).in(Singleton.class);
		bind(ValidatorFactory.class).to(AppValidatorFactory.class).in(
				Singleton.class);
		bind(ServerValidator.class).to(ServerValidatorImpl.class).in(
				Singleton.class);
	}

}

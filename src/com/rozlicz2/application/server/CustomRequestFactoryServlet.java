package com.rozlicz2.application.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class CustomRequestFactoryServlet extends RequestFactoryServlet {
	static class LoquaciousExceptionHandler implements ExceptionHandler {
		private static final Logger log = Logger
				.getLogger(LoquaciousExceptionHandler.class.getName());

		@Override
		public ServerFailure createServerFailure(Throwable throwable) {
			log.log(Level.SEVERE, "Server error", throwable);
			return new ServerFailure(throwable.getMessage(), throwable
					.getClass().getName(), null, true);
		}
	}

	private static final long serialVersionUID = 1L;

	public CustomRequestFactoryServlet() {
		super(new LoquaciousExceptionHandler());
	}
}

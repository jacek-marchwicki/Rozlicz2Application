package com.rozlicz2.application.client.mvp;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UncaughtExceptionEvent extends
		GwtEvent<UncaughtExceptionEvent.Handler> {

	public interface Handler extends EventHandler {
		void onUncaughtException(UncaughtExceptionEvent event);
	}

	public static final Type<Handler> TYPE = new Type<UncaughtExceptionEvent.Handler>();
	private final Throwable throwable;

	public UncaughtExceptionEvent(Throwable throwable) {
		this.throwable = throwable;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onUncaughtException(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public Throwable getThrowable() {
		return throwable;
	}

}

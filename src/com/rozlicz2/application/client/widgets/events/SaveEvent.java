package com.rozlicz2.application.client.widgets.events;

import com.google.gwt.event.shared.GwtEvent;

public class SaveEvent extends GwtEvent<SaveHandler> {

	private static final Type<SaveHandler> TYPE = new Type<SaveHandler>();

	public static <T> void fire(HasSaveHandlers source) {
		SaveEvent event = new SaveEvent();
		source.fireEvent(event);
	}

	public static Type<SaveHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveHandler handler) {
		handler.onSave(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SaveHandler> getAssociatedType() {
		return TYPE;
	}
}

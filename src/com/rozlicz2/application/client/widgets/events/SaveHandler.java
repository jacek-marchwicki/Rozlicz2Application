package com.rozlicz2.application.client.widgets.events;

import com.google.gwt.event.shared.EventHandler;

public interface SaveHandler extends EventHandler {
	void onSave(SaveEvent saveEvent);
}
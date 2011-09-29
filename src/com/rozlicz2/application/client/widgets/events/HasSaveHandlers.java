package com.rozlicz2.application.client.widgets.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasSaveHandlers extends HasHandlers {
	HandlerRegistration addSaveHandler(SaveHandler handler);
}

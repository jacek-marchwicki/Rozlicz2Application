package com.rozlicz2.application.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.rozlicz2.application.shared.proxy.ContactProxy;

public class ContactListEvent extends GwtEvent<ContactListEvent.Handler> {

	public interface Handler extends EventHandler {
		void onContactList(ContactListEvent event);
	}

	public static final Type<Handler> TYPE = new Type<ContactListEvent.Handler>();
	private final List<ContactProxy> contacts;

	public ContactListEvent(List<ContactProxy> contacts) {
		this.contacts = contacts;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onContactList(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public List<ContactProxy> getContacts() {
		return contacts;
	}

}

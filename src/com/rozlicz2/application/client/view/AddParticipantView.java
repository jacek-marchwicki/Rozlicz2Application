package com.rozlicz2.application.client.view;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.shared.proxy.ContactProxy;

public interface AddParticipantView extends IsWidget {
	public static interface Presenter {
		void addedUsers(Collection<String> usersIds);

		void cancel();
	}

	public void center();

	public void setContacts(List<ContactProxy> contacts);

	public void setPresenter(Presenter presenter);
}

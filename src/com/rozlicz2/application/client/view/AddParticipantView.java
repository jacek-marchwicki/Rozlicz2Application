package com.rozlicz2.application.client.view;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface AddParticipantView extends IsWidget {
	public void setUsersList(List<String> users);
	public void setPresenter(Presenter presenter);
	public void center();
	public static interface Presenter {
		void addedUsers(Collection<String> users);
		void cancel();
	}
}

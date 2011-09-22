package com.rozlicz2.application.client.activity;

import java.util.Collection;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.place.AddParticipantPlace;
import com.rozlicz2.application.client.view.AddParticipantView;

public class AddParticipantActivity extends AbstractActivity implements
		AddParticipantView.Presenter {
	private final ClientFactory clientFactory;
	private final Place previousPlace;

	public AddParticipantActivity(AddParticipantPlace place,
			ClientFactory clientFactory) {
		previousPlace = place.getPreviousPlace();
		this.clientFactory = clientFactory;
	}

	@Override
	public void addedUsers(Collection<String> users) {
		// TODO Do something with users
		clientFactory.getPlaceController().goTo(previousPlace);
	}

	@Override
	public void cancel() {
		clientFactory.getPlaceController().goTo(previousPlace);

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		AddParticipantView view = clientFactory.getAddParticipantView();
		view.setPresenter(this);
		panel.setWidget(view.asWidget());
		view.center();
	}

}

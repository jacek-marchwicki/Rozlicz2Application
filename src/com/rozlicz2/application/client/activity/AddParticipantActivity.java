package com.rozlicz2.application.client.activity;

import java.util.Collection;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.place.AddParticipantPlace;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;

public class AddParticipantActivity extends AbstractActivity implements
		AddParticipantView.Presenter {
	private final ClientFactory clientFactory;
	private final Place previousPlace;
	private final String projectId;
	private final ListwidgetRequestFactory rf;

	public AddParticipantActivity(AddParticipantPlace place,
			ClientFactory clientFactory) {
		previousPlace = place.getPreviousPlace();
		projectId = place.getProjectId();
		rf = clientFactory.getRf();
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

	private void getContactList() {
		// TODO Auto-generated method stub

	}

	private void getProjectById() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		AddParticipantView view = clientFactory.getAddParticipantView();
		view.setPresenter(this);
		panel.setWidget(view.asWidget());
		getProjectById();
		getContactList();
		view.center();
	}

}

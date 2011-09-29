package com.rozlicz2.application.client.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.rozlicz2.application.client.event.ContactListEvent;
import com.rozlicz2.application.client.event.ProjectChangedEvent;
import com.rozlicz2.application.client.place.AddParticipantPlace;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.shared.proxy.ContactProxy;
import com.rozlicz2.application.shared.proxy.ParticipantEntityProxy;
import com.rozlicz2.application.shared.proxy.ProjectProxy;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ContactRequestContext;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ProjectRequestContext;
import com.rozlicz2.application.shared.tools.IdGenerator;

public class AddParticipantActivity extends AbstractActivity implements
		AddParticipantView.Presenter {
	private AddParticipantView addParticipantView;
	private EventBus eventBus;
	protected PlaceController placeController;
	private Place previousPlace;
	private ProjectProxy project = null;
	private String projectId;
	private ResettableEventBus resettableEventBus;
	private ListwidgetRequestFactory rf;
	private Collection<String> usersToAdd = null;

	public AddParticipantActivity() {
	}

	@Override
	public void addedUsers(Collection<String> users) {
		usersToAdd = users;
		if (project == null)
			return; // wait until project will be returned
		realAddUsersToProject(project, usersToAdd);

	}

	@Override
	public void cancel() {
		placeController.goTo(previousPlace);
	}

	private void getContactList(final EventBus eventBus) {
		ContactRequestContext contactRequest = rf.getContactRequest();
		contactRequest.listAll().fire(new Receiver<List<ContactProxy>>() {

			@Override
			public void onSuccess(List<ContactProxy> arg0) {
				ContactListEvent event = new ContactListEvent(arg0);
				eventBus.fireEvent(event);
			}
		});
	}

	private void getProjectById(final EventBus eventBus) {
		ProjectRequestContext projectRequest = rf.getProjectRequest();
		projectRequest.find(projectId).fire(new Receiver<ProjectProxy>() {

			@Override
			public void onSuccess(ProjectProxy arg0) {
				ProjectChangedEvent event = new ProjectChangedEvent(arg0);
				eventBus.fireEvent(event);
			}
		});
	}

	@Override
	public void onStop() {
		resettableEventBus.removeHandlers();
	}

	private void realAddUsersToProject(ProjectProxy readOnlyProject,
			Collection<String> usersToAdd) {
		ProjectRequestContext projectRequest = rf.getProjectRequest();
		ProjectProxy project = projectRequest.edit(readOnlyProject);
		List<ParticipantEntityProxy> participants = project.getParticipants();
		if (participants == null)
			participants = new ArrayList<ParticipantEntityProxy>();
		for (String user : usersToAdd) {
			boolean found = false;

			for (ParticipantEntityProxy participant : participants) {
				if (participant.getName().equals(user)) {
					found = true;
					break;
				}
			}
			if (found)
				continue;
			ParticipantEntityProxy participant = projectRequest
					.create(ParticipantEntityProxy.class);
			participant.setId(IdGenerator.nextId());
			participant.setName(user);
			participants.add(participant);
		}
		project.setParticipants(participants);
		projectRequest.save(project).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				placeController.goTo(previousPlace);
			}
		});

	}

	public void setAddParticipantView(AddParticipantView addParticipantView) {
		this.addParticipantView = addParticipantView;
	}

	@Inject
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setPlace(AddParticipantPlace place) {
		previousPlace = place.getPreviousPlace();
		projectId = place.getProjectId();
	}

	@Inject
	public void setPlaceController(PlaceController placeController) {
		this.placeController = placeController;
	}

	@Inject
	public void setRf(ListwidgetRequestFactory rf) {
		this.rf = rf;
	}

	@Override
	public void start(AcceptsOneWidget panel,
			com.google.gwt.event.shared.EventBus e) {
		resettableEventBus = new ResettableEventBus(this.eventBus);
		resettableEventBus.addHandler(ContactListEvent.TYPE,
				new ContactListEvent.Handler() {

					@Override
					public void onContactList(ContactListEvent event) {
						updateContacts(event.getContacts());
					}
				});
		resettableEventBus.addHandler(ProjectChangedEvent.TYPE,
				new ProjectChangedEvent.Handler() {

					@Override
					public void onProjectChanged(ProjectChangedEvent event) {
						if (!event.getProject().getId().equals(projectId))
							return;
						updateProject(event.getProject());
						if (usersToAdd != null)
							realAddUsersToProject(event.getProject(),
									usersToAdd);
					}
				});
		addParticipantView.setPresenter(this);
		panel.setWidget(addParticipantView.asWidget());
		addParticipantView.center();

		getProjectById(eventBus);
		getContactList(eventBus);
	}

	protected void updateContacts(List<ContactProxy> contacts) {
		addParticipantView.setContacts(contacts);
	}

	protected void updateProject(ProjectProxy project) {
		this.project = project;
	}

}

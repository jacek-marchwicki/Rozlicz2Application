package com.rozlicz2.application.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.rozlicz2.application.shared.proxy.ProjectProxy;

public class ProjectChangedEvent extends GwtEvent<ProjectChangedEvent.Handler> {

	public interface Handler extends EventHandler {
		void onProjectChanged(ProjectChangedEvent event);
	}

	public final static Type<Handler> TYPE = new Type<ProjectChangedEvent.Handler>();
	private ProjectProxy project;

	public ProjectChangedEvent(ProjectProxy project) {
		this.project = project;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onProjectChanged(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public ProjectProxy getProject() {
		return project;
	}

	public void setProject(ProjectProxy project) {
		this.project = project;
	}

}

package com.rozlicz2.application.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.rozlicz2.application.shared.proxy.ProjectListProxy;

public class ProjectsListEvent extends GwtEvent<ProjectsListEvent.Handler> {

	public interface Handler extends EventHandler {
		void onProjectsList(ProjectsListEvent event);
	}

	public static final Type<ProjectsListEvent.Handler> TYPE = new Type<ProjectsListEvent.Handler>();
	private final List<ProjectListProxy> projectsList;

	public ProjectsListEvent(List<ProjectListProxy> projectsList) {
		this.projectsList = projectsList;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onProjectsList(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public List<ProjectListProxy> getReadOnlyProjectsList() {
		return projectsList;
	}

}

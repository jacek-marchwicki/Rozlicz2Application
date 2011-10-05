package com.rozlicz2.application.server.service;

import com.rozlicz2.application.shared.entity.Project;

public interface ProjectDao {

	public abstract Project uFind(String id);

	public abstract void uRemoveList(Project list);

	public abstract void uSave(Project list);

	public abstract Project uSaveAndReturn(Project list);

}
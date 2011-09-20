package com.rozlicz2.application.server.service;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.AppUser;
import com.rozlicz2.application.shared.entity.Project;
import com.rozlicz2.application.shared.entity.ProjectList;

public class ProjectDao extends ObjectifyDao<Project> {
	static {
		ObjectifyService.register(Project.class);
	}

	private final ProjectListDao projectListDao;

	@Inject
	public ProjectDao(ProjectListDao projectListDao) {
		this.projectListDao = projectListDao;
	}

	@Override
	public Project find(String id) {
		Project find = super.find(id);
		AppUser currentUser = getCurrentUser();
		if (currentUser == null)
			return null;
		if (find == null)
			return null;
		if (find.getOwner().equals(currentUser.getKey()))
			return find;
		return null;
	}

	public void removeList(Project list) {
		this.delete(list);
		projectListDao.delete(new ProjectList(list));
	}

	public void save(Project list) {
		AppUser currentUser = getCurrentUser();
		list.setOwner(currentUser);
		put(list);
		projectListDao.put(new ProjectList(list));
	}

	public Project saveAndReturn(Project list) {
		AppUser currentUser = getCurrentUser();
		list.setOwner(currentUser);
		Key<Project> key = put(list);
		projectListDao.put(new ProjectList(list));
		try {
			return this.get(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

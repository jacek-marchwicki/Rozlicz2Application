package com.rozlicz2.application.server.service;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.AppUser;
import com.rozlicz2.application.shared.entity.ParticipantEntity;
import com.rozlicz2.application.shared.entity.Project;
import com.rozlicz2.application.shared.entity.ProjectList;

public class ProjectDao extends ObjectifyDao<Project> {
	static {
		ObjectifyService.register(Project.class);
	}

	private final ExpenseDao expenseDao;
	private final ProjectListDao projectListDao;

	@Inject
	public ProjectDao(ProjectListDao projectListDao, ExpenseDao expenseDao) {
		this.projectListDao = projectListDao;
		this.expenseDao = expenseDao;
	}

	@Override
	public Project find(String id) {
		Project find = super.find(id);
		AppUser currentUser = getCurrentUser();
		if (currentUser == null)
			return null;
		if (find == null)
			return null;
		Key<AppUser> userKey = new Key<AppUser>(AppUser.class,
				currentUser.getId());
		if (find.getOwner().equals(userKey))
			return find;
		return null;
	}

	public void removeList(Project list) {
		this.delete(list);
		projectListDao.delete(new ProjectList(list));
	}

	public void save(Project list) {
		saveAndReturn(list);
	}

	public Project saveAndReturn(Project list) {
		Project old = super.find(list.getId());
		AppUser currentUser = getCurrentUser();
		Key<AppUser> userKey = new Key<AppUser>(AppUser.class,
				currentUser.getId());
		list.setOwner(userKey);
		if (list.getParticipants() == null)
			list.setParticipants(new ArrayList<ParticipantEntity>());
		Key<Project> key = put(list);
		projectListDao.put(new ProjectList(list));
		if (old == null || old.getParticipants() == null
				|| !old.getParticipants().equals(list.getParticipants())) {
			expenseDao.refreshParticipantsForProject(list.getParticipants(),
					list.getId());
		}
		try {
			return this.get(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
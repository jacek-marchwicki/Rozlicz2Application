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

	public Project uFind(String id) {
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

	public void uRemoveList(Project list) {
		this.delete(list);
		projectListDao.delete(new ProjectList(list));
	}

	public void uSave(Project list) {
		uSaveAndReturn(list);
	}

	public Project uSaveAndReturn(Project list) {
		Project old = super.find(list.getId());
		AppUser currentUser = getCurrentUser();
		Key<AppUser> userKey = new Key<AppUser>(AppUser.class,
				currentUser.getId());
		list.setOwner(userKey);
		if (list.getParticipants() == null)
			list.setParticipants(new ArrayList<ParticipantEntity>());
		if (old == null || old.getParticipants() == null
				|| !old.getParticipants().equals(list.getParticipants())) {
			double sum = expenseDao.refreshParticipantsForProject(
					list.getParticipants(), list.getId());
			list.setSum(sum);
		}

		Key<Project> key = put(list);
		projectListDao.put(new ProjectList(list));
		try {
			return this.get(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

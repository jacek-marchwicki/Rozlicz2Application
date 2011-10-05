package com.rozlicz2.application.server.service;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.AppUser;
import com.rozlicz2.application.shared.entity.ParticipantEntity;
import com.rozlicz2.application.shared.entity.Project;
import com.rozlicz2.application.shared.entity.ProjectList;

public class ProjectDaoImpl extends ObjectifyDao<Project> implements ProjectDao {
	static {
		ObjectifyService.register(Project.class);
	}

	private final ExpenseDao expenseDao;
	private final ProjectListDaoImpl projectListDao;

	@Inject
	public ProjectDaoImpl(ProjectListDaoImpl projectListDao, ExpenseDao expenseDao) {
		this.projectListDao = projectListDao;
		this.expenseDao = expenseDao;
	}

	/* (non-Javadoc)
	 * @see com.rozlicz2.application.server.service.ProjectDao#uFind(java.lang.String)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see com.rozlicz2.application.server.service.ProjectDao#uRemoveList(com.rozlicz2.application.shared.entity.Project)
	 */
	@Override
	public void uRemoveList(Project list) {
		this.delete(list);
		projectListDao.delete(new ProjectList(list));
	}

	/* (non-Javadoc)
	 * @see com.rozlicz2.application.server.service.ProjectDao#uSave(com.rozlicz2.application.shared.entity.Project)
	 */
	@Override
	public void uSave(Project list) {
		uSaveAndReturn(list);
	}

	/* (non-Javadoc)
	 * @see com.rozlicz2.application.server.service.ProjectDao#uSaveAndReturn(com.rozlicz2.application.shared.entity.Project)
	 */
	@Override
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

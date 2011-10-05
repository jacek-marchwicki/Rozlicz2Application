package com.rozlicz2.application.server.service;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.AppUser;
import com.rozlicz2.application.shared.entity.ProjectList;

public class ProjectListDaoImpl extends ObjectifyDao<ProjectList> implements ProjectListDao {
	static {
		ObjectifyService.register(ProjectList.class);
	}

	/* (non-Javadoc)
	 * @see com.rozlicz2.application.server.service.ProjectListDao#removeList(com.rozlicz2.application.shared.entity.ProjectList)
	 */
	@Override
	public void removeList(ProjectList list) {
		this.delete(list);
	}

	/* (non-Javadoc)
	 * @see com.rozlicz2.application.server.service.ProjectListDao#uFindUser(java.lang.String)
	 */
	@Override
	public ProjectList uFindUser(String projectId) {
		ProjectList find = find(projectId);
		if (find == null)
			return null;
		AppUser currentUser = getCurrentUser();
		if (currentUser == null)
			return null;
		Key<AppUser> userKey = new Key<AppUser>(AppUser.class,
				currentUser.getId());
		if (!find.getOwner().equals(userKey))
			return null;
		return find;
	}

	/* (non-Javadoc)
	 * @see com.rozlicz2.application.server.service.ProjectListDao#uListAll()
	 */
	@Override
	public List<ProjectList> uListAll() {
		return super.listAllForUser();
	}
}

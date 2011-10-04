package com.rozlicz2.application.server.service;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.AppUser;
import com.rozlicz2.application.shared.entity.ProjectList;

public class ProjectListDao extends ObjectifyDao<ProjectList> {
	static {
		ObjectifyService.register(ProjectList.class);
	}

	public void removeList(ProjectList list) {
		this.delete(list);
	}

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

	public List<ProjectList> uListAll() {
		return super.listAllForUser();
	}
}

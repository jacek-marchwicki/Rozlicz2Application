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

	/**
	 * Remove a list. Since items are embedded, they are removed automatically.
	 * 
	 * @param list
	 */

	public ProjectList findUser(String projectId) {
		ProjectList find = find(projectId);
		if (find == null)
			return null;
		AppUser currentUser = getCurrentUser();
		if (currentUser == null)
			return null;
		if (!find.getOwner().equals(currentUser.getKey()))
			return null;
		return find;
	}

	@Override
	public List<ProjectList> listAll() {
		return super.listAllForUser();
	}

	public void removeList(ProjectList list) {
		this.delete(list);
	}

	public void save(ProjectList list) {
		AppUser currentUser = getCurrentUser();
		list.setOwner(currentUser);
		put(list);
	}

	public ProjectList saveAndReturn(ProjectList list) {
		AppUser currentUser = getCurrentUser();
		list.setOwner(currentUser);
		Key<ProjectList> key = put(list);
		try {
			return this.get(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

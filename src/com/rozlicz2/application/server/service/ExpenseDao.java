package com.rozlicz2.application.server.service;

import java.util.List;

import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.Expense;
import com.rozlicz2.application.shared.entity.ProjectList;

public class ExpenseDao extends ObjectifyDao<Expense> {

	static {
		ObjectifyService.register(Expense.class);
	}

	private final ProjectListDao projectListDao;

	@Inject
	public ExpenseDao(ProjectListDao projectListDao) {
		this.projectListDao = projectListDao;
	}

	@Override
	public Expense find(String id) {
		// TODO check users
		Expense find = super.find(id);
		return find;
	}

	public List<Expense> findByProjectId(String projectId) {
		ProjectList projectList = projectListDao.findUser(projectId);
		if (projectList == null)
			return null;
		List<Expense> list = this.listByProperty("projectId", projectId);
		return list;
	}

	public void save(Expense list) {
		String projectId = list.getProjectId();
		if (projectId == null)
			return;
		ProjectList findUser = projectListDao.findUser(projectId);
		if (findUser == null)
			return;
		put(list);
	}

}

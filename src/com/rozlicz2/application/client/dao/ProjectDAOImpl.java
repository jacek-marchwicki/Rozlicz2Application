package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectDAOImpl implements ProjectDAO{
private HashMap<Long , Project> projects = new HashMap<Long, Project>();
	public ProjectDAOImpl() {
		Project project = new Project();
		project.setId(123456);
		project.setName("Moj pierwszy projekt");
		project.setExpensesShort(new ArrayList<ExpenseShort>());
		projects.put(new Long(project.getId()), project);
	}
	public Project getProject(long projectId) {
		return projects.get(new Long(projectId));
	}
}

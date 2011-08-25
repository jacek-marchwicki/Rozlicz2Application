package com.rozlicz2.application.client.dao;

import java.util.HashMap;

import com.rozlicz2.application.client.entity.ProjectEntity;

public class ProjectsDAOImpl implements ProjectsDAO{
	private HashMap<Long , ProjectEntity> projects = new HashMap<Long, ProjectEntity>();
	
	public ProjectEntity getProject(long projectId) {
		return projects.get(new Long(projectId));
	}
	@Override
	public void addProject(ProjectEntity project) {
		projects.put(new Long(project.getId()), project);
	}
	@Override
	public void removeProject(ProjectEntity project) {
		projects.remove(new Long(project.getId()));
	}
}

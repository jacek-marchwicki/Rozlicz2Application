package com.rozlicz2.application.client.dao;

import com.rozlicz2.application.client.entity.ProjectEntity;

public interface ProjectsDAO {
	public ProjectEntity getProject(long projectId) ;
	public void addProject(ProjectEntity project);
	public void removeProject(ProjectEntity project);
}

package com.rozlicz2.application.client.dao;

import java.util.List;

import com.rozlicz2.application.client.entity.ProjectShortEntity;


public interface ProjectsShortDAO {
	public List<ProjectShortEntity> getAll();
	public void addProject(ProjectShortEntity project);
	public void removeProject(ProjectShortEntity project);
	public int getCount();

}

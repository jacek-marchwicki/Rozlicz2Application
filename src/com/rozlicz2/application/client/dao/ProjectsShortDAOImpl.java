package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.List;

import com.rozlicz2.application.client.entity.ProjectShortEntity;


public class ProjectsShortDAOImpl implements ProjectsShortDAO {
	private List<ProjectShortEntity> all =
		new ArrayList<ProjectShortEntity>();
	
	@Override
	public List<ProjectShortEntity> getAll() {
		return all;
	}

	@Override
	public void addProject(ProjectShortEntity project) {
		all.add(project);
	}

	@Override
	public void removeProject(ProjectShortEntity project) {
		for (ProjectShortEntity entity : all) {
			if (entity.getId() == project.getId()) {
				all.remove(entity);
				return;
			}
		}
	}

	@Override
	public int getCount() {
		return getAll().size();
	}

	@Override
	public void save(ProjectShortEntity project) {
		removeProject(project);
		addProject(project);
	}
}

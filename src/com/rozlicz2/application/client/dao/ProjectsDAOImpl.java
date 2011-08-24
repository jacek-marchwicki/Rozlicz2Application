package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.List;

import com.rozlicz2.application.shared.ProjectEntity;

public class ProjectsDAOImpl implements ProjectsDAO {
	private ArrayList<ProjectEntity> all =
		new ArrayList<ProjectEntity>();
	
	public ProjectsDAOImpl() {
		ProjectEntity projectEntity = new ProjectEntity("Tesco Shopping");
		projectEntity.setId((long) 123456);
		all.add(projectEntity);
		all.add(new ProjectEntity("Travel to Brussel"));
	}

	public int getCount() {
		return all.size();
	}

	public List<ProjectEntity> getAll() {
		return all;
	}
}
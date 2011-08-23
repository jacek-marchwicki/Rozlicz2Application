package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.List;

public class ProjectsDAO {
	private ArrayList<ProjectEntity> all =
		new ArrayList<ProjectEntity>();
	
	public ProjectsDAO() {
		all.add(new ProjectEntity("Tesco Shopping"));
		all.add(new ProjectEntity("Travel to Brussel"));
	}

	public int getCount() {
		return all.size();
	}

	public List<ProjectEntity> getAll() {
		return all;
	}
}

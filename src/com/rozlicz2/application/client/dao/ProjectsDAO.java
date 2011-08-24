package com.rozlicz2.application.client.dao;

import java.util.List;

import com.rozlicz2.application.shared.ProjectEntity;

public interface ProjectsDAO {

	int getCount();

	List<ProjectEntity> getAll();

}

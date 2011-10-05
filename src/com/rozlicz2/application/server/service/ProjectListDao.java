package com.rozlicz2.application.server.service;

import java.util.List;

import com.rozlicz2.application.shared.entity.ProjectList;

public interface ProjectListDao {

	public abstract void removeList(ProjectList list);

	public abstract ProjectList uFindUser(String projectId);

	public abstract List<ProjectList> uListAll();

}
package com.rozlicz2.application.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.rozlicz2.application.server.locator.DaoServiceLocator;
import com.rozlicz2.application.server.service.ProjectListDao;
import com.rozlicz2.application.shared.proxy.ProjectListProxy;

public interface ListwidgetRequestFactory extends RequestFactory {
	/**
	 * Service stub for methods in ItemListDao
	 * 
	 * TODO Enhance RequestFactory to enable service stubs to extend a base
	 * interface so we don't have to repeat methods from the base ObjectifyDao
	 * in each stub
	 */
	@Service(value = ProjectListDao.class, locator = DaoServiceLocator.class)
	interface ItemListRequestContext extends RequestContext {
		Request<List<ProjectListProxy>> listAll();

		Request<Void> removeList(ProjectListProxy list);

		Request<Void> save(ProjectListProxy list);

		Request<ProjectListProxy> saveAndReturn(ProjectListProxy newList);
	}

	ItemListRequestContext projectListRequest();
}
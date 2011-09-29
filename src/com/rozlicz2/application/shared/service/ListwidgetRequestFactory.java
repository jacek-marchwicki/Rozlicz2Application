package com.rozlicz2.application.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.rozlicz2.application.server.locator.DaoServiceLocator;
import com.rozlicz2.application.server.service.ContactDao;
import com.rozlicz2.application.server.service.ExpenseDao;
import com.rozlicz2.application.server.service.ProjectDao;
import com.rozlicz2.application.server.service.ProjectListDao;
import com.rozlicz2.application.shared.proxy.ContactProxy;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;
import com.rozlicz2.application.shared.proxy.ProjectListProxy;
import com.rozlicz2.application.shared.proxy.ProjectProxy;

public interface ListwidgetRequestFactory extends RequestFactory {
	@Service(value = ContactDao.class, locator = DaoServiceLocator.class)
	interface ContactRequestContext extends RequestContext {
		Request<List<ContactProxy>> listAll();
	}

	@Service(value = ExpenseDao.class, locator = DaoServiceLocator.class)
	interface ExpenseRequestContext extends RequestContext {

		Request<ExpenseProxy> find(String id);

		Request<List<ExpenseProxy>> findByProjectId(String projectId);

		Request<Void> save(ExpenseProxy expense);

		Request<ExpenseProxy> saveAndReturn(ExpenseProxy expense);

	}

	@Service(value = ProjectListDao.class, locator = DaoServiceLocator.class)
	interface ProjectListRequestContext extends RequestContext {
		Request<List<ProjectListProxy>> listAll();

		Request<Void> removeList(ProjectListProxy list);
	}

	@Service(value = ProjectDao.class, locator = DaoServiceLocator.class)
	interface ProjectRequestContext extends RequestContext {

		Request<ProjectProxy> find(String id);

		Request<Void> removeList(ProjectProxy list);

		Request<Void> save(ProjectProxy list);

		Request<ProjectProxy> saveAndReturn(ProjectProxy newList);
	}

	ContactRequestContext getContactRequest();

	ExpenseRequestContext getExpenseRequest();

	ProjectListRequestContext getProjectListRequest();

	ProjectRequestContext getProjectRequest();
}
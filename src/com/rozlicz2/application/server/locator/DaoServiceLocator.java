package com.rozlicz2.application.server.locator;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;
import com.rozlicz2.application.server.service.AppUserSessionDao;
import com.rozlicz2.application.server.service.ContactDao;
import com.rozlicz2.application.server.service.ExpenseDao;
import com.rozlicz2.application.server.service.ExpenseDaoImpl;
import com.rozlicz2.application.server.service.ProjectDao;
import com.rozlicz2.application.server.service.ProjectDaoImpl;
import com.rozlicz2.application.server.service.ProjectListDao;
import com.rozlicz2.application.server.service.ProjectListDaoImpl;
import com.rozlicz2.application.shared.entity.AppUser;

public class DaoServiceLocator implements ServiceLocator {
	public class DAOsModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(AppUser.class);
			bind(AppUserSessionDao.class);
			bind(ExpenseDao.class).to(ExpenseDaoImpl.class);
			bind(ProjectDao.class).to(ProjectDaoImpl.class);
			bind(ProjectListDao.class).to(ProjectListDaoImpl.class);
			bind(ContactDao.class);
		}

	}

	private final Injector injector;

	public DaoServiceLocator() {
		injector = Guice.createInjector(new DAOsModule());
	}

	@Override
	public Object getInstance(Class<?> clazz) {
		return injector.getInstance(clazz);
	}

}

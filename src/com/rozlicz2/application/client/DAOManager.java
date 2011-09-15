package com.rozlicz2.application.client;

import com.rozlicz2.application.client.dao.SyncDatastoreService;
import com.rozlicz2.application.client.dao.SyncEntity;
import com.rozlicz2.application.client.dao.SyncKey;
import com.rozlicz2.application.client.dao.SyncObserver;
import com.rozlicz2.application.client.entity.ExpenseEntity;
import com.rozlicz2.application.client.entity.IdMap;

public class DAOManager {
	public static final String EXPANSE = "1";
	public static final String EXPANSE_CONSUMERS = "12"; // IdMap<ExpanseConsumer>
	public static final String EXPANSE_NAME = "2"; // String
	public static final String EXPANSE_PAYMENTS = "11"; // IdMap<ExpensePayment>
	public static final String EXPANSE_PROJECTID = "3"; // Long
	public static final String EXPANSE_SUM = "13"; // Double

	public static final String GLOBAL = "4";
	public static final String GLOBAL_PROJECTCOUNT = "5"; // Integer

	public static final String PROJECT = "6";
	public static final String PROJECT_EXPENSES = "7"; // IdMap<ExpenseShortEntity>
	public static final String PROJECT_NAME = "8"; // String
	public static final String PROJECT_PARTICIPANTS = "14"; // IdMap<Participants>

	public static final String PROJECTSHORT = "9";
	public static final String PROJECTSHORT_NAME = "10"; // String
	public static final String USER = "15";
	public static final String USER_NAME = "16"; // String

	private final SyncDatastoreService dao;
	private final SyncObserver expenseObserver = new SyncObserver() {

		@Override
		public void changed(SyncEntity before, SyncEntity after) {
			SyncEntity entity = after != null ? after : before;
			assert (entity != null);
			long expenseId = entity.getKey().getId();
			String expenseName = (String) entity.getProperty(EXPANSE_NAME);
			Long projectId = (Long) entity
					.getProperty(DAOManager.EXPANSE_PROJECTID);
			assert (projectId != null);
			SyncKey projectK = new SyncKey(PROJECT, projectId);
			assert (projectK != null);
			SyncEntity projectE = dao.get(projectK);
			assert (projectE != null);
			@SuppressWarnings("unchecked")
			IdMap<ExpenseEntity> projectExpenses = (IdMap<ExpenseEntity>) projectE
					.getProperty(PROJECT_EXPENSES);
			assert (projectExpenses != null);

			ExpenseEntity shortExpense = new ExpenseEntity();
			shortExpense.setName(expenseName);
			shortExpense.setId(expenseId);

			projectExpenses.remove(shortExpense);
			if (after != null) {
				projectExpenses.put(shortExpense);
			}
			projectE.setProperty(PROJECT_EXPENSES, projectExpenses);
			dao.put(projectE);
		}
	};

	private final SyncKey globalKey;

	private final SyncObserver projectObserver = new SyncObserver() {

		@Override
		public void changed(SyncEntity before, SyncEntity after) {
			SyncEntity global = dao.get(globalKey);
			Integer property = (Integer) global
					.getProperty(DAOManager.GLOBAL_PROJECTCOUNT);
			if (before == null && after != null) {
				int value = property.intValue() + 1;
				global.setProperty(DAOManager.GLOBAL_PROJECTCOUNT, new Integer(
						value));
				dao.put(global);
			}
			if (before != null && after == null) {
				int value = property.intValue() - 1;
				global.setProperty(DAOManager.GLOBAL_PROJECTCOUNT, new Integer(
						value));
				dao.put(global);
			}
			SyncEntity entity = before != null ? before : after;
			assert (entity != null);
			long id = entity.getKey().getId();
			SyncKey projectShortK = new SyncKey(DAOManager.PROJECTSHORT, id);
			if (after == null) {
				dao.delete(projectShortK);
			} else {
				String projectName = (String) after
						.getProperty(DAOManager.PROJECT_NAME);
				assert (projectName != null);
				SyncEntity projectShortE = new SyncEntity(projectShortK);
				projectShortE.setProperty(DAOManager.PROJECTSHORT_NAME,
						projectName);
				dao.put(projectShortE);
			}
		}
	};

	public DAOManager(SyncDatastoreService dao) {
		this.dao = dao;
		{
			SyncEntity global = new SyncEntity(DAOManager.GLOBAL);
			global.setProperty(DAOManager.GLOBAL_PROJECTCOUNT, new Integer(0));
			dao.put(global);
			globalKey = global.getKey();
		}
		dao.addObserver(DAOManager.EXPANSE, expenseObserver);
		dao.addObserver(DAOManager.PROJECT, projectObserver);
	}
}

package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.entity.ExpenseEntity;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.view.ExpenseView;

public class ExpenseActivity extends AbstractActivity implements ExpenseView.Presenter {

	private final ExpensePlace place;
	private final ClientFactory clientFactory;
	private ExpenseEntity expense;
	public ExpenseActivity(ExpensePlace place, ClientFactory clientFactory) {
		this.place = place;
		this.clientFactory = clientFactory;
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		expense = clientFactory.getExpensesDAO().getExpense(place.getExpenseId());
		if (expense == null) {
			Place place = new NotFoundPlace();
			clientFactory.getPlaceController().goTo(place);
			return;
		}
		ExpenseView view = clientFactory.getExpenseView();
		view.setPresenter(this);
		view.setExpenseName(expense.getName());
		panel.setWidget(view.asWidget());
	}
	@Override
	public void setExpenseName(String value) {
		expense.setName(value);
		clientFactory.getProjectsDAO().getProject(expense.getProjectId()).updateExpenseShort(expense);
		clientFactory.getExpensesDAO().save(expense);
	}

}

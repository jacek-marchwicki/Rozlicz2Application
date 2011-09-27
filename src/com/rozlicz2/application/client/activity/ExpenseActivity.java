package com.rozlicz2.application.client.activity;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.event.ExpenseChangedEvent;
import com.rozlicz2.application.client.place.AddParticipantPlace;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.view.ExpenseView;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ExpenseRequestContext;

public class ExpenseActivity extends AbstractActivity implements
		ExpenseView.Presenter {

	private ResettableEventBus childEventBus;

	private final ClientFactory clientFactory;
	private ExpenseProxy expense;

	private ExpenseRequestContext expenseRequest;

	private ExpenseView expenseView;

	private final ExpensePlace place;

	private final ListwidgetRequestFactory rf;

	public ExpenseActivity(ExpensePlace place, ClientFactory clientFactory) {
		this.place = place;
		this.clientFactory = clientFactory;
		rf = clientFactory.getRf();
	}

	@Override
	public void addParticipants() {
		Place place = new AddParticipantPlace(this.place,
				expense.getProjectId());
		clientFactory.getPlaceController().goTo(place);
	}

	private void getExpenseById(final EventBus eventBus, String expenseId) {
		rf.getExpenseRequest().find(expenseId).with("payments", "consumers")
				.fire(new Receiver<ExpenseProxy>() {

					@Override
					public void onSuccess(ExpenseProxy response) {
						if (response == null) {
							notFoundPlace();
							return;
						}
						ExpenseChangedEvent event = new ExpenseChangedEvent(
								response);
						eventBus.fireEvent(event);
					}

				});
	}

	private void notFoundPlace() {
		Place place = new NotFoundPlace();
		clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void onCancel() {
		// maybe remove handlers
	}

	@Override
	public void onStop() {
		childEventBus.removeHandlers();
	}

	@Override
	public void save() {
		expense = expenseView.getDriver().flush();
		expenseRequest.save(expense).fire(new Receiver<Void>() {
			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				expenseView.getDriver().setConstraintViolations(violations);
			}

			@Override
			public void onSuccess(Void response) {
			}
		});
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		childEventBus = new ResettableEventBus(eventBus);
		childEventBus.addHandler(ExpenseChangedEvent.TYPE,
				new ExpenseChangedEvent.Handler() {

					@Override
					public void onChange(ExpenseChangedEvent expenseChangedEvent) {
						ExpenseProxy readOnlyExpense = expenseChangedEvent
								.getReadOnlyExpense();
						if (!(place.getExpenseId().equals(readOnlyExpense
								.getId())))
							return;
						updateExpense(readOnlyExpense);
					}
				});

		expenseView = clientFactory.getExpenseView();
		expenseView.setPresenter(this);

		ExpenseProxy readOnlyExpense = place.getExpense();
		if (readOnlyExpense == null) {
			String expenseId = place.getExpenseId();
			getExpenseById(eventBus, expenseId);
		} else {
			updateExpense(readOnlyExpense);
		}

		panel.setWidget(expenseView.asWidget());
	}

	protected void updateExpense(ExpenseProxy readOnlyExpense) {
		expenseRequest = rf.getExpenseRequest();
		expense = expenseRequest.edit(readOnlyExpense);
		expenseView.getDriver().edit(expense);
		Double sum = readOnlyExpense.getSum();
		expenseView.setSum(sum);
	}

}

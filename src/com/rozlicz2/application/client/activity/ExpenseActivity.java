package com.rozlicz2.application.client.activity;

import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.validation.client.Validation;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
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

	private EventBus eventBus;
	private ExpenseProxy expense;

	private ExpensePlace place;

	private PlaceController placeController;

	private ListwidgetRequestFactory rf;

	private Request<Void> saveContext;

	private ExpenseView view;

	public ExpenseActivity() {
	}

	@Override
	public void addParticipants() {
		Place place = new AddParticipantPlace(this.place,
				expense.getProjectId());
		placeController.goTo(place);
	}

	protected void expenseChanged(ExpenseProxy readOnlyExpense) {
		ExpenseRequestContext expenseRequest = rf.getExpenseRequest();
		expense = expenseRequest.edit(readOnlyExpense);
		view.getDriver().edit(expense, expenseRequest);
		Double sum = readOnlyExpense.getSum();
		view.setSum(sum);
		view.setLocked(false);
		view.savable(false);
	}

	private void getExpenseById(final EventBus eventBus, String expenseId) {
		rf.getExpenseRequest().uFind(expenseId).with("payments", "consumers")
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
		placeController.goTo(place);
	}

	@Override
	public void onCancel() {
		view.setLocked(false);
	}

	@Override
	public void onStop() {
		view.setLocked(false);
		childEventBus.removeHandlers();
	}

	@Override
	public void save() {
		validate();
		if (view.getDriver().hasErrors())
			return;
		ExpenseRequestContext requestContext = (ExpenseRequestContext) view
				.getDriver().flush();
		if (requestContext.isChanged()) {
			saveContext = null;
			requestContext.fire();
			expenseChanged(expense);
		}
	}

	@Inject
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Inject
	public void setExpenseView(ExpenseView expenseView) {
		this.view = expenseView;
	}

	public void setPlace(ExpensePlace place) {
		this.place = place;
	}

	@Inject
	public void setPlaceController(PlaceController placeController) {
		this.placeController = placeController;
	}

	@Inject
	public void setRf(ListwidgetRequestFactory rf) {
		this.rf = rf;
	}

	@Override
	public void start(AcceptsOneWidget panel,
			com.google.gwt.event.shared.EventBus e) {
		childEventBus = new ResettableEventBus(eventBus);
		childEventBus.addHandler(ExpenseChangedEvent.TYPE,
				new ExpenseChangedEvent.Handler() {

					@Override
					public void onExpenseChange(ExpenseChangedEvent expenseChangedEvent) {
						ExpenseProxy readOnlyExpense = expenseChangedEvent
								.getReadOnlyExpense();
						if (!(place.getExpenseId().equals(readOnlyExpense
								.getId())))
							return;
						expenseChanged(readOnlyExpense);
					}
				});

		view.setPresenter(this);
		view.setLocked(true);
		view.savable(false);

		ExpenseProxy readOnlyExpense = place.getExpense();
		if (readOnlyExpense == null) {
			String expenseId = place.getExpenseId();
			getExpenseById(eventBus, expenseId);
		} else {
			expenseChanged(readOnlyExpense);
		}

		panel.setWidget(view.asWidget());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate() {
		ExpenseRequestContext requestContext = (ExpenseRequestContext) view
				.getDriver().flush();
		if (view.getDriver().hasErrors()) {
			view.savable(false);
			return;
		}
		Configuration<?> configuration = Validation.byDefaultProvider()
				.configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ExpenseProxy>> validate = validator
				.validate(expense);

		Iterable<?> violations = validate;
		view.getDriver().setConstraintViolations(
				(Iterable<ConstraintViolation<?>>) violations);
		if (validate.size() > 0) {
			view.savable(false);
			return;
		}
		if (saveContext == null)
			saveContext = requestContext.uSave(expense);
		view.savable(requestContext.isChanged());
	}

}

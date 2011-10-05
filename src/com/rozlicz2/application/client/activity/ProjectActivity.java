package com.rozlicz2.application.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.rozlicz2.application.client.event.ExpensesChangedEvent;
import com.rozlicz2.application.client.event.ProjectChangedEvent;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.resources.ApplicationConstants;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.shared.entity.Expense.PaymentOption;
import com.rozlicz2.application.shared.proxy.ExpenseConsumerEntityProxy;
import com.rozlicz2.application.shared.proxy.ExpensePaymentEntityProxy;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;
import com.rozlicz2.application.shared.proxy.ParticipantEntityProxy;
import com.rozlicz2.application.shared.proxy.ProjectProxy;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ExpenseRequestContext;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ProjectRequestContext;
import com.rozlicz2.application.shared.tools.IdGenerator;

public class ProjectActivity extends AbstractActivity implements
		ProjectView.Presenter {

	private ResettableEventBus childEventBus;
	private ValidatorFactory factory;
	private ProjectPlace place;
	private PlaceController placeController;

	private ProjectProxy project;
	private ListwidgetRequestFactory rf;
	private Request<Void> saveContext = null;
	private ProjectView view;

	public ProjectActivity() {
	}

	@Override
	public void createExpense() {
		ExpenseRequestContext expenseRequest = rf.getExpenseRequest();
		ExpenseProxy expense = expenseRequest.create(ExpenseProxy.class);
		expense.setId(IdGenerator.nextId());
		expense.setName(ApplicationConstants.constants.newExpense());
		expense.setPayments(getEmptyExpensePayment(expenseRequest));
		expense.setConsumers(getEmptyExpenseConsumer(expenseRequest));
		expense.setProjectId(place.getProjectId());
		expense.setPaymentOption(PaymentOption.EVERYBODY);
		expenseRequest.uSave(expense).fire();
		ExpensePlace place = new ExpensePlace(expense);
		placeController.goTo(place);

	}

	protected void drawExpenses(List<ExpenseProxy> expenses) {
		view.setExpenses(expenses);
	}

	@Override
	public void editExpense(ExpenseProxy expense) {
		ExpensePlace place = new ExpensePlace(expense);
		placeController.goTo(place);
	}

	private void findProjectById(final EventBus eventBus) {
		ProjectRequestContext projectRequest = rf.getProjectRequest();
		ExpenseRequestContext expenseRequest = rf.getExpenseRequest();
		expenseRequest = projectRequest.append(expenseRequest);
		projectRequest.uFind(place.getProjectId()).with("participants")
				.to(new Receiver<ProjectProxy>() {

					@Override
					public void onSuccess(ProjectProxy response) {
						if (response == null) {
							placeController.goTo(new NotFoundPlace());
						}
						ProjectChangedEvent projectChangedEvent = new ProjectChangedEvent(
								response);
						eventBus.fireEvent(projectChangedEvent);
					}
				});
		expenseRequest.uFindByProjectId(place.getProjectId())
				.with("payments", "consumers")
				.to(new Receiver<List<ExpenseProxy>>() {

					@Override
					public void onSuccess(List<ExpenseProxy> response) {
						ExpensesChangedEvent event = new ExpensesChangedEvent(
								place.getProjectId(), response);
						eventBus.fireEvent(event);
					}
				});
		expenseRequest.fire();
	}

	public List<ExpenseConsumerEntityProxy> getEmptyExpenseConsumer(
			ExpenseRequestContext expenseRequest) {
		ArrayList<ExpenseConsumerEntityProxy> list = new ArrayList<ExpenseConsumerEntityProxy>();
		for (ParticipantEntityProxy participant : project.getParticipants()) {
			ExpenseConsumerEntityProxy consumer = expenseRequest
					.create(ExpenseConsumerEntityProxy.class);
			consumer.setId(participant.getId());
			consumer.setName(participant.getName());
			consumer.setConsumer(true);
			consumer.setProportional(true);
			consumer.setValue(0.0);
			list.add(consumer);
		}
		return list;
	}

	public List<ExpensePaymentEntityProxy> getEmptyExpensePayment(
			ExpenseRequestContext expenseRequest) {
		ArrayList<ExpensePaymentEntityProxy> list = new ArrayList<ExpensePaymentEntityProxy>();
		for (ParticipantEntityProxy participant : project.getParticipants()) {
			ExpensePaymentEntityProxy payment = expenseRequest
					.create(ExpensePaymentEntityProxy.class);
			payment.setId(participant.getId());
			payment.setName(participant.getName());
			payment.setValue(0.0);
			list.add(payment);
		}
		return list;
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

	protected void projectChanged(ProjectProxy readOnlyProject) {
		ProjectRequestContext projectRequest = rf.getProjectRequest();
		project = projectRequest.edit(readOnlyProject);
		view.getDriver().edit(project, projectRequest);
		view.setLocked(false);
		view.savable(false);
	}

	@Override
	public void save() {
		validate();
		if (view.getDriver().hasErrors())
			return;
		ProjectRequestContext requestContext = (ProjectRequestContext) view
				.getDriver().flush();
		if (requestContext.isChanged()) {
			saveContext = null;
			requestContext.fire();
			projectChanged(project);
		}

	}

	public void setPlace(ProjectPlace place) {
		this.place = place;
	}

	@Inject
	public void setPlaceController(PlaceController placeController) {
		this.placeController = placeController;
	}

	@Inject
	public void setProjectView(ProjectView projectView) {
		this.view = projectView;
	}

	@Inject
	public void setRf(ListwidgetRequestFactory rf) {
		this.rf = rf;
	}

	@Inject
	public void setValidatorFactory(ValidatorFactory factory) {
		this.factory = factory;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.childEventBus = new ResettableEventBus(eventBus);
		childEventBus.addHandler(ProjectChangedEvent.TYPE,
				new ProjectChangedEvent.Handler() {

					@Override
					public void onProjectChanged(ProjectChangedEvent event) {
						ProjectProxy readOnlyProject = event.getProject();
						if (place.getProjectId()
								.equals(readOnlyProject.getId()))
							projectChanged(readOnlyProject);
					}
				});
		childEventBus.addHandler(ExpensesChangedEvent.TYPE,
				new ExpensesChangedEvent.Handler() {

					@Override
					public void onChended(
							ExpensesChangedEvent expenseChangedEvent) {
						if (place.getProjectId() != expenseChangedEvent
								.getProjectId())
							return;
						List<ExpenseProxy> expenses = expenseChangedEvent
								.getExpenses();
						drawExpenses(expenses);
					}
				});

		view.setPresenter(this);
		view.setLocked(true);
		view.savable(false);
		drawExpenses(new ArrayList<ExpenseProxy>());

		ProjectProxy readOnlyProject = place.getProject();
		if (readOnlyProject != null) {
			projectChanged(readOnlyProject);
		} else {
			findProjectById(eventBus);
		}

		panel.setWidget(view.asWidget());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate() {
		ProjectRequestContext requestContext = (ProjectRequestContext) view
				.getDriver().flush();
		if (view.getDriver().hasErrors()) {
			view.savable(false);
			return;
		}

		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ProjectProxy>> validate = validator
				.validate(project);

		Iterable<?> violations = validate;
		view.getDriver().setConstraintViolations(
				(Iterable<ConstraintViolation<?>>) violations);
		if (validate.size() > 0) {
			view.savable(false);
			return;
		}
		if (saveContext == null)
			saveContext = requestContext.uSave(project);
		view.savable(requestContext.isChanged());
	}
}

package com.rozlicz2.application.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.validation.client.Validation;
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
import com.rozlicz2.application.shared.proxy.ProjectProxy;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ExpenseRequestContext;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ProjectRequestContext;
import com.rozlicz2.application.shared.tools.IdGenerator;

public class ProjectActivity extends AbstractActivity implements
		ProjectView.Presenter {

	private ResettableEventBus childEventBus;
	private ProjectPlace place;
	private PlaceController placeController;
	private ProjectProxy project;

	private ProjectView projectView;
	private ListwidgetRequestFactory rf;

	public ProjectActivity() {
	}

	@Override
	public void createExpense() {
		ExpenseRequestContext expenseRequest = rf.getExpenseRequest();
		ExpenseProxy expense = expenseRequest.create(ExpenseProxy.class);
		expense.setId(IdGenerator.nextId());
		expense.setName(ApplicationConstants.constants.newExpense());
		expense.setPayments(new ArrayList<ExpensePaymentEntityProxy>());
		expense.setConsumers(new ArrayList<ExpenseConsumerEntityProxy>());
		expense.setProjectId(place.getProjectId());
		expense.setPaymentOption(PaymentOption.EVERYBODY);
		expenseRequest.save(expense).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Window.alert("Created");
			}
		});
		ExpensePlace place = new ExpensePlace(expense);
		placeController.goTo(place);
	}

	protected void drawExpenses(List<ExpenseProxy> expenses) {
		projectView.setExpenses(expenses);
	}

	@Override
	public void editExpense(ExpenseProxy expense) {
		ExpensePlace place = new ExpensePlace(expense);
		placeController.goTo(place);
	}

	private void findProjectById(final EventBus eventBus) {
		rf.getProjectRequest().find(place.getProjectId())
				.fire(new Receiver<ProjectProxy>() {

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
		rf.getExpenseRequest().findByProjectId(place.getProjectId())
				.with("payments", "consumers")
				.fire(new Receiver<List<ExpenseProxy>>() {

					@Override
					public void onSuccess(List<ExpenseProxy> response) {
						ExpensesChangedEvent event = new ExpensesChangedEvent(
								place.getProjectId(), response);
						eventBus.fireEvent(event);
					}
				});
	}

	@Override
	public void onCancel() {
		projectView.setLocked(false);
	}

	@Override
	public void onStop() {
		projectView.setLocked(false);
		childEventBus.removeHandlers();
	}

	protected void projectChanged(ProjectProxy readOnlyProject) {
		ProjectRequestContext projectRequest = rf.getProjectRequest();
		project = projectRequest.edit(readOnlyProject);
		projectView.getDriver().edit(project, projectRequest);
		projectView.setLocked(false);
	}

	@Override
	public void save() {
		validate();
		if (projectView.getDriver().hasErrors())
			return;
		projectView.setLocked(true);
		ProjectRequestContext projectContext = (ProjectRequestContext) projectView
				.getDriver().flush();

		Request<ProjectProxy> projectPersistRequest = projectContext
				.saveAndReturn(project);
		projectPersistRequest.fire(new Receiver<ProjectProxy>() {

			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				projectView.getDriver().setConstraintViolations(violations);
			}

			@Override
			public void onSuccess(ProjectProxy readOnlyProject) {
				projectChanged(readOnlyProject);
			}
		});

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
		this.projectView = projectView;
	}

	@Inject
	public void setRf(ListwidgetRequestFactory rf) {
		this.rf = rf;
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

		projectView.setPresenter(this);
		projectView.setLocked(true);
		drawExpenses(new ArrayList<ExpenseProxy>());

		ProjectProxy readOnlyProject = place.getProject();
		if (readOnlyProject != null) {
			projectChanged(readOnlyProject);
		} else {
			findProjectById(eventBus);
		}

		panel.setWidget(projectView.asWidget());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate() {
		projectView.getDriver().flush();
		Configuration<?> configuration = Validation.byDefaultProvider()
				.configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ProjectProxy>> validate = validator
				.validate(project);

		Iterable<?> violations = validate;
		projectView.getDriver().setConstraintViolations(
				(Iterable<ConstraintViolation<?>>) violations);
	}
}

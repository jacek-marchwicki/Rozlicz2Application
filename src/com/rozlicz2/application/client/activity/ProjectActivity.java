package com.rozlicz2.application.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.rozlicz2.application.client.ClientFactory;
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
	private final ClientFactory clientFactory;
	private final ProjectPlace place;
	private ProjectProxy project;
	private ProjectRequestContext projectRequest;
	private ProjectView projectView;
	private final ListwidgetRequestFactory rf;

	public ProjectActivity(ProjectPlace place, ClientFactory clientFactory) {
		this.place = place;
		this.clientFactory = clientFactory;
		rf = clientFactory.getRf();
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
		clientFactory.getPlaceController().goTo(place);
	}

	protected void drawExpenses(List<ExpenseProxy> expenses) {
		projectView.setExpenses(expenses);
	}

	@Override
	public void editExpense(ExpenseProxy expense) {
		ExpensePlace place = new ExpensePlace(expense);
		clientFactory.getPlaceController().goTo(place);
	}

	private void findProjectById(final EventBus eventBus) {
		rf.getProjectRequest().find(place.getProjectId())
				.fire(new Receiver<ProjectProxy>() {

					@Override
					public void onSuccess(ProjectProxy response) {
						if (response == null) {
							// project not found
							clientFactory.getPlaceController().goTo(
									new NotFoundPlace());
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
		// maybe remove handlers
	}

	@Override
	public void onStop() {
		childEventBus.removeHandlers();
	}

	protected void projectChanged(ProjectProxy readOnlyProject) {
		projectRequest = rf.getProjectRequest();
		project = projectRequest.edit(readOnlyProject);
		projectView.getDriver().edit(project);
	}

	@Override
	public void save() {
		project = projectView.getDriver().flush();
		projectRequest.save(project).fire(new Receiver<Void>() {

			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				projectView.getDriver().setConstraintViolations(violations);
			}

			@Override
			public void onSuccess(Void response) {
			}
		});

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

		projectView = clientFactory.getProjectView();
		projectView.setPresenter(this);
		drawExpenses(new ArrayList<ExpenseProxy>());

		ProjectProxy readOnlyProject = place.getProject();
		if (readOnlyProject != null) {
			projectChanged(readOnlyProject);
		} else {
			findProjectById(eventBus);
		}

		panel.setWidget(projectView.asWidget());
	}
}

package com.rozlicz2.application.client.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.entity.ExpenseEntity;
import com.rozlicz2.application.client.entity.ExpenseEntity.Consumer;
import com.rozlicz2.application.client.entity.ExpenseEntity.Payment;
import com.rozlicz2.application.client.entity.ProjectEntity;
import com.rozlicz2.application.client.entity.UserShortEntitiy;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.view.ExpenseView;
import com.rozlicz2.application.client.view.ExpenseView.ExpenseConsumer;
import com.rozlicz2.application.client.view.ExpenseView.ExpensePayment;

public class ExpenseActivity extends AbstractActivity implements ExpenseView.Presenter {

	private final ExpensePlace place;
	private final ClientFactory clientFactory;
	private ExpenseEntity expense;
	private ProjectEntity project;
	public ExpenseActivity(ExpensePlace place, ClientFactory clientFactory) {
		this.place = place;
		this.clientFactory = clientFactory;
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		expense = clientFactory.getExpensesDAO().getExpense(place.getExpenseId());
		project = clientFactory.getProjectsDAO().getProject(expense.getProjectId());
		if (expense == null) {
			Place place = new NotFoundPlace();
			clientFactory.getPlaceController().goTo(place);
			return;
		}
		ExpenseView view = clientFactory.getExpenseView();
		view.setPresenter(this);
		view.setExpenseName(expense.getName());
		
		List<ExpensePayment> payments = getPayments();
		double sum = getPaymentsSum(payments);
		List<ExpenseConsumer> consumers = getConsumers(sum);
		view.setConsumers(consumers);
		view.setPayments(payments);
		view.setPaymentsSum(sum);
		panel.setWidget(view.asWidget());
	}
	
	private double getPaymentsSum(List<ExpensePayment> payments) {
		double ret = 0.0;
		for (ExpensePayment payment : payments) {
			ret += payment.value;
		}
		return ret;
	}
	private List<ExpenseConsumer> getConsumers(double sum) {
		ArrayList<ExpenseConsumer> consumers = new ArrayList<ExpenseView.ExpenseConsumer>();
		List<UserShortEntitiy> perticipants = project.getPerticipants();
		for (UserShortEntitiy perticipant : perticipants) {
			ExpenseConsumer consumer = new ExpenseConsumer();
			consumer.userId = perticipant.getId();
			consumer.name = perticipant.getName();
			consumer.isProportional = true;
			consumer.isConsumer = false;
			
			Consumer expenseConsumer = expense.getConsumer(perticipant.getId());
			if (expenseConsumer != null) {
				consumer.isProportional = expenseConsumer.proportional;
				consumer.isConsumer = true;
				consumer.value = expenseConsumer.value;
			}
			consumers.add(consumer);
		}
		int proportionals = 0;
		for (ExpenseConsumer consumer : consumers) {
			if (!consumer.isConsumer)
				continue;
			if (consumer.isProportional)
			{
				proportionals++;
				continue;
			}
			sum -= consumer.value;
		}
		double valuePerProportional = sum / (double)proportionals;
		for (ExpenseConsumer consumer : consumers) {
			if (!consumer.isConsumer)
				continue;
			if (!consumer.isProportional)
				continue;
			consumer.value = valuePerProportional;
		}
		return consumers;
	}
	private List<ExpensePayment> getPayments() {
		ArrayList<ExpensePayment> payments = new ArrayList<ExpenseView.ExpensePayment>();
		List<UserShortEntitiy> perticipants = project.getPerticipants();
		for (UserShortEntitiy perticipant : perticipants) {
			ExpensePayment payment = new ExpensePayment();
			payment.userId = perticipant.getId();
			payment.name = perticipant.getName();
			payment.value = 0.0;
			Payment expensePayment = expense.getPayment(perticipant.getId());
			if (expensePayment != null){
				payment.value = expensePayment.value;
			}
			payments.add(payment);
		}
		return payments;
	}
	@Override
	public void setExpenseName(String value) {
		expense.setName(value);
		clientFactory.getProjectsDAO().getProject(expense.getProjectId()).updateExpenseShort(expense);
		clientFactory.getExpensesDAO().save(expense);
	}
	@Override
	public void paymentSetValue(Long userId, double value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void consumerSet(Long userId, boolean isConsumer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void consumerSetProportional(Long userId, boolean isProportional) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void consumerSetValue(Long userId, double value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addParticipant() {
		// TODO Auto-generated method stub
		
	}

}

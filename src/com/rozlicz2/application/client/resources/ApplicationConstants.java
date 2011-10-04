package com.rozlicz2.application.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface ApplicationConstants extends Constants {
	static ApplicationConstants constants = GWT
			.create(ApplicationConstants.class);

	String addParticipant();

	String allowFriendsEdit();

	String cancel();

	String close();

	String emptyExpense();

	String emptyProject();

	String everybody();

	String expense();

	String expenseName();

	String expensePaidBy();

	String expenseParticipants();

	String expensesList();

	String getConsumer();

	String gwtUser();

	String inviteFriends();

	String logout();

	String nameLabel();

	String newExpense();

	String newProject();

	String onlyMe();

	String projectName();

	String proportional();

	String remoteProceduralCall();

	String removeUser();

	String save();

	String selectedUsers();

	String sendingTheNameToServer();

	String userName();

	String value();
}

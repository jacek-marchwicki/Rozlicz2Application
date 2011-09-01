package com.rozlicz2.application.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface ApplicationConstants extends Constants {
	static ApplicationConstants constants = GWT.create(ApplicationConstants.class);
	
	String gwtUser();
	String remoteProceduralCall();
	String sendingTheNameToServer();
	String close();
	String nameLabel();
	String emptyProject();
	String emptyExpense();
	String expense();
	String newExpense();
	String expenseParticipants();
	String expensePaidBy();
	String newProject();
	String expensesList();
	String expenseName();
	String projectName();
	String cancel();
	String save();
	String allowFriendsEdit();
	String inviteFriends();
	String addParticipant();
}

package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.dao.ExpenseShort;

public class ProjectViewImpl extends Composite implements ProjectView {

	private static ProjectViewImplUiBinder uiBinder = GWT
			.create(ProjectViewImplUiBinder.class);

	interface ProjectViewImplUiBinder extends UiBinder<Widget, ProjectViewImpl> {
	}

	public ProjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Label projectNameLabel;

	public ProjectViewImpl(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setProjectName(String projectName) {
		projectNameLabel.setText(projectName);	
	}

	@Override
	public void setExpenses(List<ExpenseShort> expenses) {
		// TODO Auto-generated method stub
		
	}

}

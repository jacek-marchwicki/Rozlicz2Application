package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.Validator;
import com.rozlicz2.application.client.Validator.ValidatorException;
import com.rozlicz2.application.client.entity.ExpenseShortEntity;
import com.rozlicz2.application.client.resources.ApplicationConstants;

public class ProjectViewImpl extends CompositeExtender implements ProjectView {

	private static ProjectViewImplUiBinder uiBinder = GWT
			.create(ProjectViewImplUiBinder.class);

	interface ProjectViewImplUiBinder extends UiBinder<Widget, ProjectViewImpl> {
	}

	public ProjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Label projectNameLabel;
	
	@UiField
	TextBox projectNameTextBox;

	@UiField
	public Label projectNameErrorLabel;

	public ProjectViewImpl(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("projectNameTextBox")
	void onProjectNameTextBoxKeyUp(KeyUpEvent e) {
		validateProjectNameTextBox();
	}
	
	private void validateProjectNameTextBox() {
		String text = projectNameTextBox.getText();
		try {
			Validator.isNotToShort(text);
		} catch (ValidatorException e) {
			printError(projectNameErrorLabel,e.getMessage(), projectNameTextBox);
			return;
		}
		clearError(projectNameErrorLabel, projectNameTextBox);
	}

	@UiHandler("projectNameTextBox")
	void onProjectNameTextBoxChange(ChangeEvent e) {
		validateProjectNameTextBox();
	}
	
	@Override
	public void setProjectName(String projectName) {
		projectNameLabel.setText(ApplicationConstants.constants.nameLabel());	
		projectNameTextBox.setText(projectName);
	}

	@Override
	public void setExpenses(List<ExpenseShortEntity> expenses) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(Presenter presenter) {
		
	}

	@Override
	protected void validator() {
		validateIsNotToShort(projectNameTextBox, projectNameErrorLabel);
	}

	private void validateIsNotToShort(TextBox projectNameTextBox,
			Label projectNameErrorLabel) {
		// TODO Auto-generated method stub
		
	}

}

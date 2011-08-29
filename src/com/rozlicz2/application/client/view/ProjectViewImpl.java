package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.rozlicz2.application.client.EntityProvidesKey;
import com.rozlicz2.application.client.entity.ExpenseShortEntity;

public class ProjectViewImpl extends Composite implements ProjectView {

	private static ProjectViewImplUiBinder uiBinder = GWT
			.create(ProjectViewImplUiBinder.class);

	interface ProjectViewImplUiBinder extends UiBinder<Widget, ProjectViewImpl> {
	}

	public ProjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public static class ExpenditureCell extends AbstractCell<ExpenseShortEntity> {
		
		interface Template extends SafeHtmlTemplates {
			@Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name,String price);
		}
		
		public ExpenditureCell() {
			if (template == null)
				template = GWT.create(Template.class);
		}

		private static Template template;

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ExpenseShortEntity value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.append(template.productCellTemplate(value.getName(),"123,zł"));
			}
		}
	}
	
	@UiFactory CellList<ExpenseShortEntity> makeCellList() {
		EntityProvidesKey<ExpenseShortEntity> keyProvider = new EntityProvidesKey<ExpenseShortEntity>();
		
		CellList<ExpenseShortEntity> cellList = new CellList<ExpenseShortEntity>(new ExpenditureCell(),
				keyProvider);
		
		final SingleSelectionModel<ExpenseShortEntity> selectionModel = new SingleSelectionModel<ExpenseShortEntity>(
				keyProvider);
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ExpenseShortEntity selectedObject = selectionModel.getSelectedObject();
				if (selectedObject == null) 
					return;
				selectionModel.setSelected(selectedObject, false);
				onSelectedObject(selectedObject);
			}
		});
		return cellList;
	}

	protected void onSelectedObject(ExpenseShortEntity selectedObject) {
		presenter.editExpense(selectedObject.getId());
	}

	@UiField
	EditableLabelWidget projectNameWidget;
	
	@UiField
	CellList<ExpenseShortEntity> expensesList;
	
	@UiField
	Button createExpenseButton;
	
	private Presenter presenter;
	
	@UiHandler("createExpenseButton")
	void onCreateExpense(ClickEvent e) {
		presenter.createExpense();
	}
	

	public ProjectViewImpl(String projectName) {
		initWidget(uiBinder.createAndBindUi(this));
		projectNameWidget.setText(projectName);
	}

	@Override
	public void setExpenses(List<ExpenseShortEntity> expenses) {
		expensesList.setRowCount(expenses.size(), true);
		expensesList.setRowData(0, expenses);
		expensesList.redraw();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}

	@Override
	public void setProjectName(String projectName) {
		projectNameWidget.setText(projectName);
	}
	
	@UiHandler("projectNameWidget")
	public void onProjectNameChange(ValueChangeEvent<String> e) {
		this.presenter.setProjectName(e.getValue());
	}

}

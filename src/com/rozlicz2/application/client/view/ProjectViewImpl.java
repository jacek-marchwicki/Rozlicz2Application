package com.rozlicz2.application.client.view;

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
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.rozlicz2.application.client.entity.BaseEntity.EntityKeyProvider;
import com.rozlicz2.application.client.entity.ExpenseShortEntity;
import com.rozlicz2.application.client.entity.IdMap;

public class ProjectViewImpl extends Composite implements ProjectView {

	public static class ExpenditureCell extends
			AbstractCell<ExpenseShortEntity> {

		interface Template extends SafeHtmlTemplates {
			@SafeHtmlTemplates.Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name, String price);
		}

		private static Template template;

		public ExpenditureCell() {
			if (template == null)
				template = GWT.create(Template.class);
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ExpenseShortEntity value, SafeHtmlBuilder sb) {
			if (value == null)
				return;
			String name = value.getName();
			assert (name != null);
			assert (name instanceof String);
			sb.append(template.productCellTemplate(name, "123,zł"));
		}
	}

	interface ProjectViewImplUiBinder extends UiBinder<Widget, ProjectViewImpl> {
	}

	private static ProjectViewImplUiBinder uiBinder = GWT
			.create(ProjectViewImplUiBinder.class);

	@UiField
	Button createExpenseButton;

	@UiField
	CellList<ExpenseShortEntity> expensesList;

	private Presenter presenter;

	@UiField
	EditableLabelWidget projectNameWidget;

	public ProjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public ProjectViewImpl(String projectName) {
		initWidget(uiBinder.createAndBindUi(this));
		projectNameWidget.setText(projectName);
	}

	@UiFactory
	CellList<ExpenseShortEntity> makeCellList() {
		EntityKeyProvider<ExpenseShortEntity> keyProvider = new EntityKeyProvider<ExpenseShortEntity>();

		CellList<ExpenseShortEntity> cellList = new CellList<ExpenseShortEntity>(
				new ExpenditureCell(), keyProvider);

		final SingleSelectionModel<ExpenseShortEntity> selectionModel = new SingleSelectionModel<ExpenseShortEntity>(
				keyProvider);
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ExpenseShortEntity selectedObject = selectionModel
						.getSelectedObject();
				if (selectedObject == null)
					return;
				selectionModel.setSelected(selectedObject, false);
				onSelectedObject(selectedObject);
			}
		});
		return cellList;
	}

	@UiHandler("createExpenseButton")
	void onCreateExpense(ClickEvent e) {
		presenter.createExpense();
	}

	@UiHandler("projectNameWidget")
	public void onProjectNameChange(ValueChangeEvent<String> e) {
		this.presenter.setProjectName(e.getValue());
	}

	protected void onSelectedObject(ExpenseShortEntity selectedObject) {
		presenter.editExpense(selectedObject.getId());
	}

	@Override
	public void setExpenses(IdMap<ExpenseShortEntity> expenses) {
		if (expenses.getDataDisplays().contains(expensesList))
			expenses.removeDataDisplay(expensesList);
		expenses.addDataDisplay(expensesList);
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

}

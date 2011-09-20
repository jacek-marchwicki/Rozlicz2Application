package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;
import com.rozlicz2.application.shared.proxy.ProjectProxy;

public class ProjectViewImpl extends Composite implements ProjectView,
		Editor<ProjectProxy> {

	interface Driver extends
			SimpleBeanEditorDriver<ProjectProxy, ProjectViewImpl> {
	}

	public static class ExpenditureCell extends AbstractCell<ExpenseProxy> {

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
				ExpenseProxy value, SafeHtmlBuilder sb) {
			if (value == null)
				return;
			sb.append(template.productCellTemplate(value.getName(), "123,zł"));
		}
	}

	public static class ExpensesEditor extends CellList<ExpenseProxy> {
		private static final ProvidesKey<ExpenseProxy> keyProvider = new ProvidesKey<ExpenseProxy>() {

			@Override
			public Object getKey(ExpenseProxy item) {
				return item.getId();
			}
		};

		public ExpensesEditor() {
			super(new ExpenditureCell(), keyProvider);
		}
	}

	interface ProjectViewImplUiBinder extends UiBinder<Widget, ProjectViewImpl> {
	}

	private static ProjectViewImplUiBinder uiBinder = GWT
			.create(ProjectViewImplUiBinder.class);

	@UiField
	Button createExpenseButton;

	Driver driver = GWT.create(Driver.class);

	@UiField
	ExpensesEditor expensesEditor;

	@UiField
	EditableLabelWidget nameEditor;

	private Presenter presenter;

	public ProjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		driver.initialize(this);

		final SingleSelectionModel<ExpenseProxy> selectionModel = new SingleSelectionModel<ExpenseProxy>(
				ExpensesEditor.keyProvider);
		expensesEditor.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ExpenseProxy selectedObject = selectionModel
						.getSelectedObject();
				if (selectedObject == null)
					return;
				selectionModel.setSelected(selectedObject, false);
				onSelectedObject(selectedObject);
			}
		});
	}

	@Override
	public SimpleBeanEditorDriver<ProjectProxy, ?> getDriver() {
		return driver;
	}

	@UiHandler("createExpenseButton")
	void onCreateExpense(ClickEvent e) {
		presenter.createExpense();
	}

	@UiHandler("nameEditor")
	public void onNameEditorChange(ValueChangeEvent<String> event) {
		presenter.save();
	}

	protected void onSelectedObject(ExpenseProxy selectedObject) {
		presenter.editExpense(selectedObject);
	}

	@Override
	public void setExpenses(List<ExpenseProxy> expenses) {
		expensesEditor.setRowData(expenses);
		expensesEditor.setRowCount(expenses.size());
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

}

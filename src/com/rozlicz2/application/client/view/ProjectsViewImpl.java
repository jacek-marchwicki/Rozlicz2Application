package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.rozlicz2.application.client.resources.CellListResources;
import com.rozlicz2.application.client.resources.LocalizedMessages;
import com.rozlicz2.application.client.widgets.LockWidget;
import com.rozlicz2.application.shared.proxy.ProjectListProxy;

public class ProjectsViewImpl extends Composite implements ProjectsView {

	public static class ProductCell extends AbstractCell<ProjectListProxy> {
		interface Template extends SafeHtmlTemplates {

			@SafeHtmlTemplates.Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name, SafeHtml price);
		}

		private static Template template;

		public ProductCell() {
			if (template == null) {
				template = GWT.create(Template.class);
			}
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ProjectListProxy value, SafeHtmlBuilder sb) {
			if (value != null) {
				String name = value.getName();
				Double sum = value.getSum();
				SafeHtml sumString = LocalizedMessages.messages.price(sum);
				sb.append(template.productCellTemplate(name, sumString));
			}
		}
	}

	interface ProjectsViewImplUiBinder extends
			UiBinder<Widget, ProjectsViewImpl> {
	}

	private static ProjectsViewImplUiBinder uiBinder = GWT
			.create(ProjectsViewImplUiBinder.class);

	@UiField
	CellList<ProjectListProxy> cellList;

	@UiField
	LockWidget lockWidget;
	private ProjectsView.Presenter presenter;

	@UiField
	Label projectsNumberLabel;

	public ProjectsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("createButton1")
	void handleCreateButton1Click(ClickEvent e) {
		presenter.createProject();
	}

	@UiHandler("createButton2")
	void handleCreateButton2Click(ClickEvent e) {
		presenter.createProject();
	}

	@UiFactory
	CellList<ProjectListProxy> makeCellList() {
		CellList<ProjectListProxy> cellList = new CellList<ProjectListProxy>(
				new ProductCell(), CellListResources.INSTANCE);

		final NoSelectionModel<ProjectListProxy> selectionModel = new NoSelectionModel<ProjectListProxy>();
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ProjectListProxy selectedObject = selectionModel
						.getLastSelectedObject();
				if (selectedObject == null)
					return;
				onSelectedObject(selectedObject);
			}
		});
		return cellList;
	}

	@UiHandler("errorGeneratorButton")
	public void onErrorGeneratorButtonClicked(ClickEvent e) {
		this.presenter.generateError();
	}

	protected void onSelectedObject(ProjectListProxy selectedObject) {
		presenter.editProject(selectedObject);
	}

	@Override
	public void setLocked(boolean locked) {
		lockWidget.setVisible(locked);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setProjectsList(List<ProjectListProxy> dataProvider) {
		cellList.setRowData(dataProvider);
	}

	@Override
	public void setProjectsNumber(int numberOfProjects) {
		projectsNumberLabel.setText(LocalizedMessages.messages
				.numberOfProjects(numberOfProjects));
	}

}

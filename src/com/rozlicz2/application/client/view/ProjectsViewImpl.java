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
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.rozlicz2.application.client.EntityProvidesKey;
import com.rozlicz2.application.client.resources.NormalMessages;
import com.rozlicz2.application.shared.ProjectEntity;

public class ProjectsViewImpl extends Composite implements ProjectsView{

	private static ProjectsViewImplUiBinder uiBinder = GWT
			.create(ProjectsViewImplUiBinder.class);

	interface ProjectsViewImplUiBinder extends UiBinder<Widget, ProjectsViewImpl> {
	}

	public ProjectsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		cellList.redraw();
	}

	@UiField
	CellList<ProjectEntity> cellList;

	@UiField
	Label projectsNumberLabel;

	private ProjectsView.Presenter presenter;

	public static class ProductCell extends AbstractCell<ProjectEntity> {
		interface Template extends SafeHtmlTemplates {
			@Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name,String price);
		}

		private static Template template;

		public ProductCell() {
			if (template == null) {
				template = GWT.create(Template.class);
			}
		}

		@Override
		public void render(Context context, ProjectEntity value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.append(template.productCellTemplate(value.getName(),"123,zł"));
			}
		}
	}

	@UiFactory CellList<ProjectEntity> makeCellList() {
		EntityProvidesKey<ProjectEntity> keyProvider = new EntityProvidesKey<ProjectEntity>();

		// Create a CellList using the keyProvider.
		CellList<ProjectEntity> cellList = new CellList<ProjectEntity>(new ProductCell(),
				keyProvider);

		// Push data into the CellList.


		// Add a selection model using the same keyProvider.
		SelectionModel<ProjectEntity> selectionModel = new SingleSelectionModel<ProjectEntity>(
				keyProvider);
		cellList.setSelectionModel(selectionModel);
		return cellList;
	}

	@UiHandler("createButton")
	void handleCreateButtonClick(ClickEvent e) {
		presenter.createProject();
	}

	@Override
	public void setUserName(String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setProjectsList(List<ProjectEntity> projectsDetails) {
		cellList.setRowCount(projectsDetails.size(), true);
		cellList.setRowData(0, projectsDetails);
		cellList.redraw();
	}

	@Override
	public void setProjectsNumber(int numberOfProjects) {
		projectsNumberLabel.setText(NormalMessages.messages.numberOfProjects(numberOfProjects));
	}

}

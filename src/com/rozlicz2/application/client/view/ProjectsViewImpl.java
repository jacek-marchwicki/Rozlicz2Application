package com.rozlicz2.application.client.view;

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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.rozlicz2.application.client.DAO;
import com.rozlicz2.application.client.EntityProvidesKey;
import com.rozlicz2.application.client.dao.AbstractEntityProvider;
import com.rozlicz2.application.client.dao.SyncEntity;
import com.rozlicz2.application.client.resources.LocalizedMessages;

public class ProjectsViewImpl extends Composite implements ProjectsView {

	public static class ProductCell extends AbstractCell<SyncEntity> {
		interface Template extends SafeHtmlTemplates {

			@SafeHtmlTemplates.Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name, String price);
		}

		private static Template template;

		public ProductCell() {
			if (template == null) {
				template = GWT.create(Template.class);
			}
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				SyncEntity value, SafeHtmlBuilder sb) {
			if (value != null) {
				String name = (String) value.getProperty(DAO.PROJECTSHORT_NAME);
				assert (name != null);
				assert (name instanceof String);
				sb.append(template.productCellTemplate(name, "123,13 PLN"));
			}
		}
	}

	interface ProjectsViewImplUiBinder extends
			UiBinder<Widget, ProjectsViewImpl> {
	}

	private static ProjectsViewImplUiBinder uiBinder = GWT
			.create(ProjectsViewImplUiBinder.class);

	@UiField
	CellList<SyncEntity> cellList;

	private ProjectsView.Presenter presenter;

	@UiField
	Label projectsNumberLabel;

	public ProjectsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("createButton")
	void handleCreateButtonClick(ClickEvent e) {
		presenter.createProject();
	}

	@UiFactory
	CellList<SyncEntity> makeCellList() {
		EntityProvidesKey keyProvider = new EntityProvidesKey();

		CellList<SyncEntity> cellList = new CellList<SyncEntity>(
				new ProductCell(), keyProvider);

		final SingleSelectionModel<SyncEntity> selectionModel = new SingleSelectionModel<SyncEntity>(
				keyProvider);
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				SyncEntity selectedObject = selectionModel.getSelectedObject();
				if (selectedObject == null)
					return;
				selectionModel.setSelected(selectedObject, false);
				onSelectedObject(selectedObject);
			}
		});
		return cellList;
	}

	protected void onSelectedObject(SyncEntity selectedObject) {
		presenter.editProject(selectedObject.getKey());
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setProjectsList(AbstractEntityProvider dataProvider) {
		dataProvider.addDataDisplay(cellList);
	}

	@Override
	public void setProjectsNumber(int numberOfProjects) {
		projectsNumberLabel.setText(LocalizedMessages.messages
				.numberOfProjects(numberOfProjects));
	}

	@Override
	public void setUserName(String userName) {
		// TODO Auto-generated method stub
	}

}

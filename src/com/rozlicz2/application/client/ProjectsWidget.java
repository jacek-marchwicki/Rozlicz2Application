package com.rozlicz2.application.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.rozlicz2.application.client.dao.ProjectEntity;
import com.rozlicz2.application.client.dao.ProjectsDAO;

public class ProjectsWidget extends Composite{

	private static ProjectsWidgetUiBinder uiBinder = GWT
	.create(ProjectsWidgetUiBinder.class);

	interface ProjectsWidgetUiBinder extends UiBinder<Widget, ProjectsWidget> {
	}

	public ProjectsWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		cellList.redraw();
	}
	
	@UiField
	CellList<ProjectEntity> cellList;
	
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
		ProjectsDAO projectsDAO = new ProjectsDAO();
		EntityProvidesKey<ProjectEntity> keyProvider = new EntityProvidesKey<ProjectEntity>();

		// Create a CellList using the keyProvider.
		CellList<ProjectEntity> cellList = new CellList<ProjectEntity>(new ProductCell(),
				keyProvider);

		// Push data into the CellList.
		cellList.setRowCount(projectsDAO.getCount(), true);
		cellList.setRowData(0, projectsDAO.getAll());

		// Add a selection model using the same keyProvider.
		SelectionModel<ProjectEntity> selectionModel = new SingleSelectionModel<ProjectEntity>(
				keyProvider);
		cellList.setSelectionModel(selectionModel);
		cellList.redraw();
		return cellList;
	}

}

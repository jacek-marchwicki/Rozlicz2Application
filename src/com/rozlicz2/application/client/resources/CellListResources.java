package com.rozlicz2.application.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Style;

public interface CellListResources extends CellList.Resources {
	public interface CellListStyle extends CellList.Style {
		String DEFAULT_CSS = "CellList.css";
	}

	public static final CellListResources INSTANCE = GWT
			.create(CellListResources.class);

	@Override
	@Source(CellListResources.CellListStyle.DEFAULT_CSS)
	Style cellListStyle();
}
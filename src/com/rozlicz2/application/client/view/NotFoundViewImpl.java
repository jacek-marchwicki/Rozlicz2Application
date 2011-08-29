package com.rozlicz2.application.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NotFoundViewImpl extends Composite implements NotFoundView {

	private static NotFoundViewImplUiBinder uiBinder = GWT
			.create(NotFoundViewImplUiBinder.class);

	interface NotFoundViewImplUiBinder extends UiBinder<Widget, NotFoundViewImpl> {
	}

	public NotFoundViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public NotFoundViewImpl(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

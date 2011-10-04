package com.rozlicz2.application.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationResources;

public class LockWidget implements IsWidget {

	interface Template extends SafeHtmlTemplates {

		@SafeHtmlTemplates.Template("<div class=\"{0}\" />")
		SafeHtml getImage(String styleName);
	}

	private static Template template;
	private final HTMLPanel panel;

	public LockWidget() {
		if (template == null) {
			template = GWT.create(Template.class);
		}
		String imageStyleName = ApplicationResources.INSTANCE.css()
				.loadingImageClass();
		SafeHtml image = template.getImage(imageStyleName);
		panel = new HTMLPanel(image);
		panel.addStyleName(ApplicationResources.INSTANCE.css()
				.lockWidgetClass());
	}

	@Override
	public Widget asWidget() {
		return panel.asWidget();
	}

	public void setVisible(boolean visible) {
		if (visible)
			panel.addStyleName(ApplicationResources.INSTANCE.css()
					.lockWidgetShowClass());
		else
			panel.removeStyleName(ApplicationResources.INSTANCE.css()
					.lockWidgetShowClass());
	}
}

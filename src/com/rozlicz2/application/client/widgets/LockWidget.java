package com.rozlicz2.application.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationConstants;
import com.rozlicz2.application.client.resources.ApplicationResources;

public class LockWidget implements IsWidget {

	interface Template extends SafeHtmlTemplates {

		@SafeHtmlTemplates.Template("<div class=\"{5}\"><img src=\"{0}\" alt=\"{1}\" class=\"{2}\" width=\"{3}\" height=\"{4}\" /></div>")
		SafeHtml getImage(SafeUri imageUri, String imageAlt, String styleName,
				int width, int height, String divClass);
	}

	private static Template template;
	private final HTMLPanel panel;

	public LockWidget() {
		if (template == null) {
			template = GWT.create(Template.class);
		}

		ImageResource loading = ApplicationResources.INSTANCE.loading();
		SafeUri safeUri = loading.getSafeUri();
		int height = loading.getHeight();
		int width = loading.getWidth();
		String imageAlt = ApplicationConstants.constants.loadingImageAlt();
		String imageStyleName = ApplicationResources.INSTANCE.css()
				.loadingImageClass();
		String divClass = ApplicationResources.INSTANCE.css()
				.loadingImageDivClass();
		SafeHtml image = template.getImage(safeUri, imageAlt, imageStyleName,
				width, height, divClass);
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

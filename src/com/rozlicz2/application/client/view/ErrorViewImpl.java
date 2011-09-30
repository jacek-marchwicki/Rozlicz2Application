package com.rozlicz2.application.client.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationResources;

public class ErrorViewImpl implements ErrorView {

	interface Template extends SafeHtmlTemplates {

		@SafeHtmlTemplates.Template("<img src=\"{0}\" class=\"{2}\" alt=\"\" />{1}")
		SafeHtml errorDisplayTemplate(SafeUri errorImageSafeUri,
				String errorInfo, String imageClass);
	}

	private static Template template;

	private Set<ErrorDisplay> containedErrros = new HashSet<ErrorView.ErrorDisplay>();

	private final SafeUri errorImageSafeUri;

	private final HTMLPanel errorsList;

	private final Map<ErrorDisplay, Widget> widgets = new HashMap<ErrorView.ErrorDisplay, Widget>();

	public ErrorViewImpl() {
		if (template == null) {
			template = GWT.create(Template.class);
		}
		errorsList = new HTMLPanel("");
		errorsList.setStyleName(ApplicationResources.INSTANCE.css()
				.errorViewClass());
		errorImageSafeUri = ApplicationResources.INSTANCE.errorImage()
				.getSafeUri();
	}

	@Override
	public Widget asWidget() {
		return errorsList.asWidget();
	}

	@Override
	public void setErrors(Set<ErrorDisplay> errors) {
		HashSet<ErrorDisplay> errorsToRemove = new HashSet<ErrorDisplay>(
				containedErrros);
		errorsToRemove.removeAll(errors);
		for (ErrorDisplay errorDisplay : errorsToRemove) {
			Widget widget = widgets.get(errorDisplay);
			errorsList.remove(widget);
			widgets.remove(widget);
		}

		HashSet<ErrorDisplay> errorsToAdd = new HashSet<ErrorView.ErrorDisplay>(
				errors);
		errorsToAdd.removeAll(containedErrros);
		String errorViewItemClass = ApplicationResources.INSTANCE.css()
				.errorViewItemClass();
		String errorViewImageClass = ApplicationResources.INSTANCE.css()
				.errorViewImageClass();
		for (ErrorDisplay errorDisplay : errorsToAdd) {
			SafeHtml html = template.errorDisplayTemplate(errorImageSafeUri,
					errorDisplay.getErrorInfo(), errorViewImageClass);
			HTML widget = new HTML(html);
			widget.setStyleName(errorViewItemClass);
			widgets.put(errorDisplay, widget);
			errorsList.add(widget);
		}
		containedErrros = new HashSet<ErrorView.ErrorDisplay>(errors);
		int height = 0;
		for (int i = 0; i < errorsList.getWidgetCount(); ++i) {
			Widget widget = errorsList.getWidget(i);
			widget.getElement().getStyle().setTop(height, Unit.PX);
			height += widget.getOffsetHeight() + 5;
		}
	}

}

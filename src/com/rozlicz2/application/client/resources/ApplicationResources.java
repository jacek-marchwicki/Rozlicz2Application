package com.rozlicz2.application.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.TextResource;

public interface ApplicationResources extends ClientBundle {
	interface ApplicationCss extends CssResource {
		String breadcrumbClass();

		String brItemClass();

		String clearClass();

		String editableWidgetLabelError();

		String errorClass();

		String errorLabelClass();

		String errorLabelShowClass();

		String errorViewClass();

		String errorViewImageClass();

		String errorViewItemClass();

		String flexTableClass();

		String flexTableEvenClass();

		String flexTableHeaderClass();

		String flexTableOddClass();

		String fullScreenClass();

		String grayBackgroundClass();

		String homeClass();

		String labelClass();

		String loadingImageClass();

		String lockWidgetClass();

		String lockWidgetShowClass();

		String saveButtonClass();
	}

	public static final ApplicationResources INSTANCE = GWT
			.create(ApplicationResources.class);

	@Source("images/checkbox.png")
	public ImageResource checkbox();

	@Source("images/checkbox-checked.png")
	public ImageResource checkboxChecked();

	@Source("images/checkbox-checked-hover.png")
	public ImageResource checkboxCheckedHover();

	@Source("images/checkbox-disabled.png")
	public ImageResource checkboxDisabled();

	@Source("images/checkbox-hover.png")
	public ImageResource checkboxHover();

	@Source("application.css")
	public ApplicationCss css();

	@Source("default.txt")
	public TextResource defaultText();

	@Source("images/error.png")
	public ImageResource errorImage();

	@Source("images/facebook.png")
	public ImageResource facebook();

	@ImageOptions()
	@Source("images/loading.gif")
	public ImageResource loading();

	@Source("images/radio.png")
	public ImageResource radio();

	@Source("images/radio-checked.png")
	public ImageResource radioChecked();

	@Source("images/radio-checked-hover.png")
	public ImageResource radioCheckedHover();

	@Source("images/radio-disabled.png")
	public ImageResource radioDisabled();

	@Source("images/radio-hover.png")
	public ImageResource radioHover();

	@Source("images/remove_user.png")
	public ImageResource removeUser();
}

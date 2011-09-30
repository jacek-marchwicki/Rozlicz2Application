package com.rozlicz2.application.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface ApplicationResources extends ClientBundle {
	interface ApplicationCss extends CssResource {
		String clearClass();

		String errorClass();

		String errorLabelClass();

		String errorLabelShowClass();

		String errorViewClass();

		String errorViewImageClass();

		String errorViewItemClass();

		String fullScreenClass();

		String grayBackgroundClass();

		String labelClass();

		String loadingImageClass();

		String loadingImageDivClass();

		String lockWidgetClass();

		String lockWidgetShowClass();
	}

	public static final ApplicationResources INSTANCE = GWT
			.create(ApplicationResources.class);

	@Source("application.css")
	public ApplicationCss css();

	@Source("default.txt")
	public TextResource defaultText();

	@Source("images/error.png")
	public ImageResource errorImage();

	@Source("images/facebook.png")
	public ImageResource facebook();

	@Source("images/loading.gif")
	public ImageResource loading();

	@Source("images/remove_user.png")
	public ImageResource removeUser();
}

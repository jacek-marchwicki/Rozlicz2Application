package com.rozlicz2.application.client.mvp;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.place.ProjectsPlace;

@WithTokenizers({ProjectsPlace.Tokenizer.class, ProjectPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}

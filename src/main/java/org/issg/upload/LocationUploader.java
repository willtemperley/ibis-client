package org.issg.upload;

import org.issg.ibis.domain.Location;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class LocationUploader extends AbstractUploader implements View {

    private UploadParser<Location> parser;

    @Inject
    public LocationUploader(Dao dao) {
        super(dao, "Upload locations");
        parser = new LocationUploadParser(dao);
    }

    @Override
    UploadParser<?> getUploadParser() {
        return parser;
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}

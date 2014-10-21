package org.issg.ibis.upload;

import org.issg.ibis.domain.SpeciesImpact;
import org.jrc.edit.Dao;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SpeciesLocationUploader extends AbstractUploader implements View {

    private UploadParser<SpeciesImpact> parser;

    @Inject
    public SpeciesLocationUploader(Dao dao) {
        super(dao, "Upload species location");
        parser = new SpeciesImpactUploadParser(dao);
    }

    @Override
    UploadParser<?> getUploadParser() {
        return parser;
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}

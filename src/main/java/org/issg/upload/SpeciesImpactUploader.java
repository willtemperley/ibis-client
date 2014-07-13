package org.issg.upload;

import org.issg.ibis.domain.SpeciesImpact;
import org.jrc.edit.Dao;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SpeciesImpactUploader extends AbstractUploader implements View {

    private UploadParser<SpeciesImpact> parser;

    @Inject
    public SpeciesImpactUploader(Dao dao) {
        super(dao, "Upload species impact");
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

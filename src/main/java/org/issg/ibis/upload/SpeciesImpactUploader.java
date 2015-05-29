package org.issg.ibis.upload;

import org.biopama.edit.Dao;
import org.issg.ibis.domain.SpeciesLocation;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SpeciesImpactUploader extends AbstractUploader implements View {

    private UploadParser<SpeciesLocation> parser;

    @Inject
    public SpeciesImpactUploader(Dao dao) {
        super(dao, "Upload species impact");
        parser = new SpeciesLocationUploadParser(dao);
    }

    @Override
    UploadParser<?> getUploadParser() {
        return parser;
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}

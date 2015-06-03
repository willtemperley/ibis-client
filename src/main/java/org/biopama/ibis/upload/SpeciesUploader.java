package org.biopama.ibis.upload;

import org.biopama.edit.Dao;
import org.issg.ibis.domain.Species;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SpeciesUploader extends AbstractUploader implements View {

    private UploadParser<Species> parser;

    @Inject
    public SpeciesUploader(Dao dao) {
        super(dao, "Upload species information");
        parser = new SpeciesUploadParser(dao);
    }

    @Override
    UploadParser<?> getUploadParser() {
        return parser;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

}

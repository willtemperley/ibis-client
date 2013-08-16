package org.issg.upload;

import org.issg.ibis.domain.Species;
import org.jrc.persist.Dao;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class ThreatSummaryUploader extends AbstractUploader implements View {

    private UploadParser<Species> parser;

    @Inject
    public ThreatSummaryUploader(Dao dao) {
        super(dao, "Upload threat summaries");
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

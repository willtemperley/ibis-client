package org.issg.excel.download;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.SpeciesImpact;
import org.jrc.persist.Dao;

import com.mysema.query.jpa.impl.JPAQuery;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;

public class ExportBar extends HorizontalLayout {


	private Species entity;
	private Link l;

	public ExportBar() {
		
		l = new Link();
		l.setCaption("Download");
		addComponent(l);
	}

	public void setEntity(Species entity) {
		this.entity = entity;
		
		l.setResource(new ExternalResource("/ibis-client/download?id=" + entity.getId()));
	}

}

package org.biopama;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.issg.ibis.domain.SiteContent;
import org.jrc.edit.Dao;
import org.jrc.ui.HtmlHeader;
import org.jrc.ui.HtmlLabel;

import java.util.List;

public class About extends VerticalLayout implements View {

	@Inject
	public About(Dao dao) {

		setSizeFull();
		
		addStyleName("content");
//
		setMargin(true);

		TabSheet ts = new TabSheet();
		ts.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
		ts.setSizeFull();
		addComponent(ts);

		List<SiteContent> l = dao.all(SiteContent.class);
		for(SiteContent sc: l) {
			HtmlLabel content = new HtmlLabel(sc.getContent());
			content.setSizeFull();
			ts.addTab(content, sc.getTitle());
		}

	}


	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}

package org.issg.ibis.upload;

import org.issg.ibis.domain.Country;
import org.issg.ibis.perspective.shared.ExportLink;
import org.issg.ibis.perspective.shared.ExportLink.ExportType;
import org.jrc.edit.Dao;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;
import org.vaadin.maddon.fields.TypedSelect;

import com.google.inject.Inject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class UploadView extends HorizontalLayout implements View {

	private Dao dao;
	private ExportLink el = new ExportLink(ExportType.XLSX);

	@Inject
	public UploadView(final Dao dao) {

		this.dao = dao;

		setSizeFull();
		setSpacing(true);

		el.setBaseURL("/ibis-client/template/country/");
		el.setVisible(false);

		Panel p = new Panel();
		p.setSizeFull();
		p.setCaption("Upload");

		SpeciesImpactUploader siUploader = new SpeciesImpactUploader(dao);
		p.setContent(siUploader);
		addComponent(p);

		Panel templatePanel = new Panel("");
		templatePanel.setSizeFull();
		templatePanel.setCaption("Download");
		VerticalLayout content = new VerticalLayout();
		templatePanel.setContent(content);
		content.addComponent(getCountrySelector());
		content.addComponent(el);
		
		addComponent(templatePanel);

	}

	private TypedSelect<Country> getCountrySelector() {
		TypedSelect<Country> select = new TypedSelect<Country>("Country")
				.withSelectType(ComboBox.class);

		select.setOptions(dao.all(Country.class));

		select.addMValueChangeListener(new MValueChangeListener<Country>() {

			@Override
			public void valueChange(MValueChangeEvent<Country> event) {
				Country c = event.getValue();
				el.setVisible(c != null);
				if (c != null) {
					el.setResourceId(c.getId());
				}
			}
		});

		return select;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}

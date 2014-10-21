package org.issg.ibis.upload;

import java.util.Set;

import org.issg.ibis.ViewModule;
import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.auth.RoleManager.Action;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class UploadView extends HorizontalLayout implements View {

	private Dao dao;
	private ExportLink el = new ExportLink(ExportType.XLSX);
	private RoleManager roleManager;

	@Inject
	public UploadView(final Dao dao, RoleManager roleManager) {

		this.dao = dao;
		this.roleManager = roleManager;

		setSizeFull();
		setSpacing(true);

		el.setBaseURL("/ibis-client/template/country/");
		el.setVisible(false);


		SpeciesImpactUploader siUploader = new SpeciesImpactUploader(dao);
		siUploader.setSizeFull();
		addComponent(siUploader);
		
		SpeciesLocationUploader slUploader = new SpeciesLocationUploader(dao);
		slUploader.setSizeFull();
		addComponent(slUploader);
		

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
		
		if (! roleManager.checkAction(UploadView.class, Action.READ)) {
			UI.getCurrent().getNavigator().navigateTo(ViewModule.UNAUTHORIZED);
		}

	}

}

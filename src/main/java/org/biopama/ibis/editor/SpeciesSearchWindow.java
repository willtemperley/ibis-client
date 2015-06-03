package org.biopama.ibis.editor;

import java.io.IOException;

import org.biopama.edit.EditorController;
import org.biopama.ibis.webservices.gbif.GbifSpeciesClient;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.json.GbifSpecies;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class SpeciesSearchWindow extends Window {

	private GbifSpeciesClient gbifSpeciesClient = new GbifSpeciesClient();

	private SpeciesInfo speciesInfo;
	
	/**
	 * Just to preview the species
	 */
	private class SpeciesInfo extends VerticalLayout {
		
		private Label label = new Label();
		
		private Label commonName = new Label();
		
		private GbifSpecies sp;

		public SpeciesInfo() {
			label.setCaption("Species");
			addComponent(label);
			addComponent(commonName);
		}

		public void setValue(GbifSpecies sp) {
			this.sp = sp;
			if (sp != null) {
				setVisible(true);
				label.setValue(sp.toString());
			}
		}

		public GbifSpecies getValue() {
			return sp;
		}
	}

	public SpeciesSearchWindow(final EditorController<Species> ec) {

		setCaption("Find species");
		setWidth(600, Unit.PIXELS);
		setHeight(600, Unit.PIXELS);
		FormLayout content = new FormLayout();
		content.setSpacing(true);
		setContent(content);

		HorizontalLayout hl = new HorizontalLayout();
		content.addComponent(hl);
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setSpacing(true);
		hl.addComponent(vl);
		hl.setSizeFull();
		hl.setSpacing(true);
		
		vl.addComponent(new Image("Search provided by:", new ThemeResource("img/gbif.png")));

		SpeciesSuggest speciesSuggest = new SpeciesSuggest();
		speciesSuggest.setCaption("Search species");
		vl.addComponent(speciesSuggest);
		speciesSuggest.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				GbifSpecies sp = (GbifSpecies) event.getProperty().getValue();
				GbifSpecies sp2 = gbifSpeciesClient.getSpecies(sp.getSpeciesKey());
				setSpecies(sp2);
			}

		});

		MTextField gbifURIField = new MTextField("GBIF URI");
		vl.addComponent(gbifURIField);
		gbifURIField
				.addMValueChangeListener(new MValueChangeListener<String>() {
					@Override
					public void valueChange(MValueChangeEvent<String> event) {
						try {
							GbifSpecies sp = gbifSpeciesClient.getSpecies(event.getValue());
							setSpecies(sp);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

		this.speciesInfo = new SpeciesInfo();
		hl.addComponent(speciesInfo);
		
		MTextField redlistId = new MTextField("Redlist ID");
		vl.addComponent(redlistId);
		redlistId.addMValueChangeListener(new MValueChangeListener<String>() {
			@Override
			public void valueChange(MValueChangeEvent<String> event) {
				GbifSpecies sp = gbifSpeciesClient.getSpeciesFromRedlist(event.getValue());

				setSpecies(sp);
			}
		});

		Button selectButton = new Button("Select");
		selectButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		speciesInfo.addComponent(selectButton);

		selectButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (speciesInfo.getValue() != null) {
					Species entity = ec.getEntity();
					entity.populate(speciesInfo.getValue());
					ec.setEntity(entity);
					SpeciesSearchWindow.this.close();
				}
			}
		});

	}
	
	@Override
	public void attach() {
		speciesInfo.setVisible(false);
		super.attach();
	}

	private void setSpecies(GbifSpecies sp) {
		speciesInfo.setValue(sp);
	}

}

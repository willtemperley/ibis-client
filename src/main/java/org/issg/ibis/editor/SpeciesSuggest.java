package org.issg.ibis.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.issg.ibis.domain.json.GbifSpecies;
import org.issg.ibis.webservices.gbif.GbifSpeciesClient;

import com.vaadin.ui.ComboBox;

public class SpeciesSuggest extends ComboBox {
	
	GbifSpeciesClient gbifSpeciesClient = new GbifSpeciesClient();
	private String newFilter = "";
	private ArrayList<GbifSpecies> dummyList;
	private GbifSpecies e;

	public SpeciesSuggest() {
		
		dummyList = new ArrayList<GbifSpecies>();
		e = new GbifSpecies();
		e.setSpecies("");
		dummyList.add(e);

	}
	
	@Override
	protected List<?> getFilteredOptions() {
		
		if (newFilter != null && newFilter.length() >= 4) {
			List<GbifSpecies> suggestions = gbifSpeciesClient.getSuggestions(newFilter);
			/*
			 * Add items to workaround the issue that the ComboBox
			 * won't accept the value has changed unless the value is in it's items.
			 */
			for (GbifSpecies gbifSpecies : suggestions) {
				addItem(gbifSpecies);
			}
			return suggestions;
		}
		e.setSpecies("Type " + (4 - newFilter.length()) + " more characters");

		return dummyList;
	}
	
	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		newFilter = (String) variables.get("filter");
		if (newFilter == null) {
			newFilter = "";
		}

		super.changeVariables(source, variables);
	}
}

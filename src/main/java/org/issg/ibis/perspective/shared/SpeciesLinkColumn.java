package org.issg.ibis.perspective.shared;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Species;
import org.jrc.ui.HtmlLabel;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class SpeciesLinkColumn implements Table.ColumnGenerator {

		public Component generateCell(Table source, Object itemId, Object columnId) {
			
			Item item = source.getItem(itemId);
			String x = (String) item.getItemProperty("link").getValue();
			return new HtmlLabel(x);
		}
	}

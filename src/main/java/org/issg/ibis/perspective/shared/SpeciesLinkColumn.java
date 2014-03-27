package org.issg.ibis.perspective.shared;

import org.issg.ibis.ViewModule;
import org.issg.ibis.domain.Species;
import org.jrc.ui.HtmlLabel;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class SpeciesLinkColumn implements Table.ColumnGenerator {

		public Component generateCell(Table source, Object itemId, Object columnId) {
			
			Species species = (Species) itemId;

			StringBuilder sb = new StringBuilder("<a href='#!");
			sb.append(ViewModule.getSpeciesLink(species));
			sb.append("'>");
			sb.append(species.getScientificName());
			sb.append("</div>");

			return new HtmlLabel(sb.toString());
		}
	}

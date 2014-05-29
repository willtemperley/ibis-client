package org.issg.ibis.perspective.shared;

import it.jrc.form.editor.EntityTable;

import java.util.List;

import javax.persistence.TypedQuery;

import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.view.ResourceDescription;
import org.issg.ibis.z.SearchSelectEventListener;
import org.jrc.persist.Dao;
import org.jrc.ui.HtmlLabel;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class SearchPanel extends VerticalLayout {

	private BeanItemContainer<ResourceDescription> bic = new BeanItemContainer<ResourceDescription>(
			ResourceDescription.class);

	private SearchSelectEventListener searchListener;

	private Dao dao;

	public SearchPanel(Dao dao) {

		addStyleName("layout-panel");

		this.dao = dao;

		setSizeFull();

		addComponent(getSearchEntityTable());
	}

	class ImpactVisualizationColumn implements Table.ColumnGenerator {

		public Component generateCell(Table source, Object itemId,
				Object columnId) {
			BeanItem<?> item = (BeanItem<?>) source.getItem(itemId);

			final ResourceDescription si = (ResourceDescription) item.getBean();
			return new SearchResult(si);
		}
	}

	public class SearchResult extends Panel {

		public SearchResult(ResourceDescription facetedSearch) {

			// ResourceType rt = facetedSearch.getResourceType();
			char initial = facetedSearch.getId().charAt(0);

			String html = "<div class='search-result result-type-" + initial
					+ "'>" + initial + "</div>";
			html += getLink(facetedSearch);

			html += ("<div>Known impacts: " + facetedSearch.getImpactCount() + "</div>");

			HtmlLabel shl = new HtmlLabel(html);

			setContent(shl);
		}

		private String getLink(ResourceDescription esi) {
			StringBuilder sb = new StringBuilder(
					"<div class='search-description'><a href='#!");
			sb.append(esi.getId());
			sb.append("'>");
			sb.append(esi.toString());
			sb.append("</a></div>");
			return sb.toString();
		}
	}

	private EntityTable<ResourceDescription> getSearchEntityTable() {

		EntityTable<ResourceDescription> table = new EntityTable<ResourceDescription>(
				bic);

		table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

		table.setHeight("100%");

		table.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(Property.ValueChangeEvent event) {

				ResourceDescription entity = (ResourceDescription) event
						.getProperty().getValue();
				// Object value = event.getProperty().getValue();
				// EntityItem<ResourceDescription> obj =
				// bic.geti.getItem(value);
				// if (value == null) {
				// return;
				// }
				// String si = (String) value;
				entitySelected(entity);

			}
		});

		ImpactVisualizationColumn generatedColumn = new ImpactVisualizationColumn();
		table.addGeneratedColumn("id", generatedColumn);
		table.setColumnWidth("id", 400);
		table.setVisibleColumns("id");
		// table.addColumns(SearchEntity_.name);
		return table;

	}

	protected void entitySelected(ResourceDescription facetedSearch) {
		if (searchListener != null) {
			searchListener.onSelect(facetedSearch);
		}
	}

	public void addSearchListener(
			SearchSelectEventListener searchSelectEventListener) {
		this.searchListener = searchSelectEventListener;
	}
}

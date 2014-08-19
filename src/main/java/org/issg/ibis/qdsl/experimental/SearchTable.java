package org.issg.ibis.qdsl.experimental;

import java.util.List;

import org.issg.ibis.domain.view.ResourceDescription;
import org.issg.ibis.qdsl.experimental.SearchSelectEventListener;
import org.jrc.edit.Dao;
import org.jrc.ui.HtmlLabel;
import org.vaadin.addons.lec.EntityTable;
import org.vaadin.maddon.ListContainer;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;

public class SearchTable extends CssLayout {

	private static final int COL_WIDTH = 400;

	private static final String TABLE_WIDTH = "450px";

	private ListContainer<ResourceDescription> bic = new ListContainer<ResourceDescription>(
			ResourceDescription.class);

	private SearchSelectEventListener searchListener;

	private Dao dao;

	public void setResults(List<ResourceDescription> l) {
		bic.removeAllItems();
		bic.addAll(l);
	}

	public SearchTable(Dao dao) {
		this.dao = dao;
		addComponent(getSearchEntityTable());
		setSizeFull();
	}

	class ImpactVisualizationColumn implements Table.ColumnGenerator {

		public Component generateCell(Table source, Object itemId,
				Object columnId) {
			
			ResourceDescription resourceDescription = (ResourceDescription) itemId;
			
			char initial = resourceDescription.getId().charAt(0);

			StringBuilder sb = new StringBuilder("<div class='search-result rt-");
			String resultType = resourceDescription.getResultType();
			
			if (resultType == null) {
				resultType = "IN";
			}

			sb.append(initial)
			.append("'>")
			.append(resultType)
			.append("</div>");

			/*
			 * The link
			 */
			sb.append("<div class='search-description'><a href='#!");
			sb.append(resourceDescription.getId());
			sb.append("'>");
			sb.append(resourceDescription.toString());
			sb.append("</a></div>");

			sb.append("<div>Known impacts: ").append(resourceDescription.getImpactCount()).append("</div>");


			return new HtmlLabel(sb.toString());
		}
	}

	private EntityTable<ResourceDescription> getSearchEntityTable() {

		EntityTable<ResourceDescription> table = new EntityTable<ResourceDescription>(bic);
		setSizeFull();

		table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

		 table.setHeight("100%");
		 table.setWidth(TABLE_WIDTH);

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
		table.setColumnWidth("id", COL_WIDTH);
		table.setVisibleColumns("id");
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

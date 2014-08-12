package org.vaadin.addons.lec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.vaadin.addons.form.util.AdminStringUtil;
import org.vaadin.maddon.ListContainer;
import org.vaadin.maddon.fields.MTable;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;

public class EntityTable<T> extends MTable<T> {

	private static final int URL_COL_WIDTH = 100;

	private List<String> visibleColumns = new ArrayList<String>();

	/**
	 * A table of entities
	 * 
	 * TODO: ordering column default??
	 * 
	 * @param container
	 */

	public EntityTable() {
	}

	public EntityTable(Class<T> clazz) {
		super(clazz);
	}

	public EntityTable(ListContainer<T> bic) {

		super(bic);
		setImmediate(true);
		setEditable(false);
		setSelectable(true);

	}

	public EntityTable(BeanItemContainer<T> beanContainer) {

		setImmediate(true);
		setEditable(false);
		setSelectable(true);

		setContainerDataSource(beanContainer);
	}

	public void refresh() {

//		if (container != null) {
//			container.refresh();
//		}
	}

	public void setDefaultSortAttribute(SingularAttribute<T, ?> prop) {
		setSortContainerPropertyId(prop.getName());
	}

	public <X> void setColumnWidth(SingularAttribute<T, X> prop, int width) {
		setColumnWidth(prop.getName(), width);
	}

	/**
	 * Adds a column with links that open in a new window.
	 * 
	 * @param prop
	 */
	public <X> void addUrlColumn(SingularAttribute<T, X> prop) {

		String propName = prop.getName();

		addGeneratedColumn(propName, new Table.ColumnGenerator() {
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				Item item = source.getItem(itemId);
				Object colValue = item.getItemProperty(columnId).getValue();

				if (colValue == null) {
					return null;
				}

				String columnValue = String.valueOf(colValue);
				Link link = new Link(columnValue, new ExternalResource(
						columnValue));
				link.setTargetName("_blank");
				return link;
			}
		});

		setColumnHeader(propName, AdminStringUtil.splitCamelCase(propName));
		setColumnWidth(propName, URL_COL_WIDTH);
	}

	/**
	 * For each of the provided {@link SingularAttribute}s adds a column to the
	 * table.
	 * 
	 * @param cols
	 */
	public void addColumns(SingularAttribute<T, ?>... cols) {

		for (SingularAttribute<T, ?> singularAttribute : cols) {
			if (singularAttribute == null) {
				throw new RuntimeException(
						"Improperly configured static metamodel.");
			}
			visibleColumns.add(singularAttribute.getName());
		}

		Object[] x = visibleColumns.toArray();
		this.setVisibleColumns(x);
		for (String string : visibleColumns) {
			this.setColumnHeader(string, AdminStringUtil.splitCamelCase(string));
		}
	}

	public void addColumns(String... cols) {

		for (String string : cols) {
			visibleColumns.add(string);
			this.setColumnHeader(string, AdminStringUtil.splitCamelCase(string));
		}
		Object[] x = visibleColumns.toArray();
		this.setVisibleColumns(x);
	}

	public void addGeneratedColumn(SingularAttribute<T, ?> attr,
			ColumnGenerator generatedColumn) {

		visibleColumns.add(attr.getName());
		super.addGeneratedColumn(attr.getName(), generatedColumn);
	}

	public void setItalicColumn(final String ... property) {
		final String[] x = property;
		setCellStyleGenerator(new CellStyleGenerator() {

			@Override
			public String getStyle(Table source, Object itemId,
					Object propertyId) {
				if (propertyId != null)  {
					for (int i = 0; i < x.length; i++) {
						if(propertyId.equals(x[i])) {
							return "species";
						}
					}
				}
				return "";
			}
		});
	}

	public void setColumnVisibility(SingularAttribute<?, ?> attr,
			boolean isVisible) {
		if (isVisible) {
			visibleColumns.add(attr.getName());
		} else {
			visibleColumns.remove(attr.getName());
		}
		Object[] x = visibleColumns.toArray();
		this.setVisibleColumns(x);
	}

}

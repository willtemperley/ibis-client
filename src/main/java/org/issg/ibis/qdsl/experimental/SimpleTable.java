package org.issg.ibis.qdsl.experimental;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.jrc.persist.Dao;
import org.jrc.server.lec.LazyEntityContainer;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.ui.Table;

public class SimpleTable<T> extends Table {

    private static final int PAGE_SIZE = 100;
	private List<String> visibleColumns = new ArrayList<String>();

	public SimpleTable() {

        setImmediate(true);
        setEditable(false);
        setMultiSelect(false);
        setSelectable(true);
        setPageLength(PAGE_SIZE);

	}

    /**
     * Adds a column to the table.
     * 
     * @param cols
     */
	public void addColumn(SingularAttribute<T, ?> prop) {
		
		String propName = prop.getName();
		visibleColumns.add(propName);
		
	}
	
	public void addGeneratedColumn(SingularAttribute<T, ?> attr, ColumnGenerator generatedColumn) {

        visibleColumns.add(attr.getName());

		super.addGeneratedColumn(attr.getName(), generatedColumn);
	}
	
	public void build() {

		Object[] x = visibleColumns.toArray();
		this.setVisibleColumns(x);
		for (String string : visibleColumns) {
		    this.setColumnHeader(string, string);
        }

	}

	
}

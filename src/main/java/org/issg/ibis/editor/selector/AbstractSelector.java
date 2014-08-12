package org.issg.ibis.editor.selector;

import java.util.List;

import javax.persistence.EntityManager;

import org.issg.ibis.responsive.TakesSelectionListener;
import org.vaadin.addons.lec.EntityTable;
import org.vaadin.maddon.ListContainer;
import org.vaadin.maddon.fields.MTable;

import com.google.inject.Provider;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.EntityPathBase;

public class AbstractSelector<T> extends EntityTable<T> implements TakesSelectionListener<T> {

	public AbstractSelector(Provider<EntityManager> emp, EntityPathBase<T> epb, ListContainer<T> container) {
		super(container);
		
		JPAQuery q = new JPAQuery(emp.get());
		q = q.from(epb);
		List<T> results = q.list(epb);
		setBeans(results);
	}
	
	
}

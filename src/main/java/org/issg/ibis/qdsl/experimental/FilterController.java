package org.issg.ibis.qdsl.experimental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.issg.ibis.domain.view.QLocationView;
import org.jrc.persist.Dao;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.StringPath;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;

/**
 * A K-V store for filter fields.
 * 
 * All it does is keep them in one place and deal with grouped operations.
 * 
 * Layout should be done elsewhere.
 * 
 * @author will
 * 
 */
public class FilterController<T> {

	private EntityPathBase<T> base;
	private Dao dao;

	private Map<StringPath, StringFilterField> fields = new HashMap<StringPath, StringFilterField>();
	private QdslQueryListener listener;

	public FilterController(EntityPathBase<T> locationview, Dao dao) {
		this.base = locationview;
		this.dao = dao;
	}

	public Component createFilterField(StringPath stringPath) {
		StringFilterField stringFilterField = new StringFilterField(stringPath);

		JPAQuery q = new JPAQuery(dao.getEntityManager());
		SearchResults<String> results = q.from(base).distinct().listResults(stringPath);

		List<String> items = results.getResults();

		for (String item : items) {
			stringFilterField.addItem(item);
		}

		if (fields.containsKey(stringPath)) {
			throw new RuntimeException(
					"Shouldn't be two fields with same path.");
		}
		fields.put(stringPath, stringFilterField);

		stringFilterField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				fireValueChanged();
			}

		});
		return stringFilterField;
	}
	
	public void addValueChangeListener(QdslQueryListener listener) {
		this.listener = listener;
	}

	private void fireValueChanged() {
		List<BooleanExpression> x = new ArrayList<BooleanExpression>();
		for (StringFilterField sff : fields.values()) {
			BooleanExpression filter = sff.getFilter();
			if (filter != null) {
				x.add(filter);
			}
		}
		listener.fireFilterChanged(x);
	}

}

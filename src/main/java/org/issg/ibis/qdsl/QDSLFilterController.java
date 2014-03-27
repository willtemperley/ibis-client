package org.issg.ibis.qdsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.SetPath;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

public class QDSLFilterController<T>  {

    private EntityPath<T> entityPath;

    private JPAQuery query;
    
    private Map<Field<?>, BooleanExpression> filterMap = new HashMap<Field<?>, BooleanExpression>();
    
    private List<BooleanExpression> filters = new ArrayList<BooleanExpression>();

    public QDSLFilterController(EntityPath<T> entityPath) {

        this.entityPath = entityPath;
        
        this.query = new JPAQuery().from(entityPath);

    }
    
    public List<T> getResults(EntityManager em) {
        
        JPAQuery q = query.clone(em);
        
        filters.clear();
        Set<Field<?>> keys = filterMap.keySet();
        for (Field<?> field : keys) {

            BooleanExpression filter = filterMap.get(field);
            if (filter != null) {
                filters.add(filter);
            }
        }
        
        SearchResults<T> x = q.where(filters.toArray(new BooleanExpression[filters.size()]))
                .listResults(entityPath);
        
        return x.getResults();
    }

    /**
     * A combo box which adds an "in" filter
     * 
     * @param cb
     * @param attribute
     */
    public <E> void addContainsField(final ComboBox cb, final SetPath<E, ? extends SimpleExpression<? super E>> attribute) {

        cb.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                
                Object value = event.getProperty().getValue();
                E obj = (E) value;
                
                if (obj == null) {
                	filterMap.remove(cb);
                } else {
                	BooleanExpression contains = attribute.contains(obj);
                	filterMap.put(cb, contains);
                }

            }
        });

    }
    
}

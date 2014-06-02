package org.jrc.server.lec;

import java.util.ArrayList;
import java.util.List;

import org.jrc.persist.Dao;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

/*
 * https://vaadin.com/forum/#!/thread/59441
 * 
 * What about an adaptable way of caching based on table size?
 * e.g. fetch strategy would be to get them all, or do so lazily
 * 
 * 
 */
public class LazyEntityContainer<T> extends ListContainer<T>{
	
	private Class<T> clazz;
	private Dao dao;

	private EntityPath<T> entityPath;

	private JPAQuery query;

	public LazyEntityContainer(EntityPath<T> entityPath, Class<T> clazz, Dao emp) {
		super(clazz);
		this.clazz = clazz;
		this.dao = emp;

        this.entityPath = entityPath;
        this.query = new JPAQuery(emp.getEntityManager()).from(entityPath);
        
        SearchResults<T> r = query.listResults(entityPath);

		backingList.addAll(r.getResults());
	}
	
    public void setFilters(List<BooleanExpression> expressions) {
        
        JPAQuery q = query.clone(dao.getEntityManager());
        
        BooleanExpression[] arr = expressions.toArray(new BooleanExpression[expressions.size()]);
        
        SearchResults<T> x = q.where(arr)
                .listResults(entityPath);
        
        backingList.clear();
        addAll(x.getResults());
    }


    
	

}

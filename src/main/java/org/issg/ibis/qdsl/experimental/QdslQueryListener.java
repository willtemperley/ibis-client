package org.issg.ibis.qdsl.experimental;

import java.util.List;

import com.mysema.query.types.expr.BooleanExpression;


public interface QdslQueryListener {
	
	public void fireFilterChanged(List<BooleanExpression> expressions);

}

package org.issg.ibis.a.event;

import org.issg.ibis.domain.FacetedSearch;

public abstract class SearchSelectEventListener {
    
    public abstract void onSelect(FacetedSearch facetedSearch);

}

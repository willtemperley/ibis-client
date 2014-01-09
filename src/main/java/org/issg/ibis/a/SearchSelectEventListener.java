package org.issg.ibis.a;

import org.issg.ibis.domain.view.FacetedSearch;
import org.issg.ibis.domain.view.ResourceDescription;

public abstract class SearchSelectEventListener {
    
    public abstract void onSelect(ResourceDescription facetedSearch);

    //FIXME REMOVE
    public void onSelect(FacetedSearch facetedSearch) {
        
    }

}

package org.issg.ibis.qdsl.search;

import org.issg.ibis.domain.view.ResourceDescription;

public abstract class SearchSelectEventListener {
    
    public abstract void onSelect(ResourceDescription facetedSearch);

}

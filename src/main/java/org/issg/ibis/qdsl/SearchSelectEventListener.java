package org.issg.ibis.qdsl;

import org.issg.ibis.domain.view.ResourceDescription;

public abstract class SearchSelectEventListener {
    
    public abstract void onSelect(ResourceDescription facetedSearch);

}

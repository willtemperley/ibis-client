package org.issg.ibis.editor;

import org.issg.ibis.domain.Species;

import com.vaadin.ui.Component;

/**
 * Various species-related components should use this
 * 
 * @author will
 *
 */
public interface ISpeciesEditor extends Component  {
	
	public void setSpecies(Species species);

}

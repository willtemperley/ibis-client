package org.biopama.edit;

import org.vaadin.addons.auth.domain.Role;

/**
 * Implemented by an entity which is owned by a user.
 * 
 * @author will
 *
 */
public interface HasRole {

    public Role getRole();

    public void setRole(Role role);

}

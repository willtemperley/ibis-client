package org.jrc.edit;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.auth.domain.Role;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.SessionScoped;

@SessionScoped
public class RoleManager {

    public enum Action {
        READ, CREATE, UPDATE, DELETE
    }

    Logger logger = LoggerFactory.getLogger(RoleManager.class);

    private Set<String> stringPermissions = new HashSet<String>();
    
    private Role role;

    private Provider<EntityManager> entityManagerProvider;

    private Role anonymousRole;

    @Inject
    public RoleManager(Provider<EntityManager> em) {

        this.entityManagerProvider = em;

        this.anonymousRole = new Role();
        
//        Object id = SecurityUtils.getSubject().getPrincipal();
        //FIXME temp
        Object id = 0l;
        if (id.equals(0l)) {
            role = anonymousRole;
        } else {
            role = getEm().find(Role.class, id);
        }

        loadPermissions();
    }

    public boolean checkPermission(Action action, String target) {
        
        if (role.getIsSuperUser()) {
            return true;
        }

        logger.debug(action + "_" + target);
        boolean hasPermission = stringPermissions.contains(action + "_"
                + target);
        logger.debug("has permission: " + hasPermission);
        return hasPermission;

    }

    public void loadPermissions() {

        stringPermissions = role.getStringPermissions();

    }

    /**
     * Simple implementation - just get the
     * 
     * @param clazz
     * @return
     */
    public String getUrlForClass(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public boolean canCreate(String target) {
        if (role.getIsSuperUser()) {
            return true;
        }
        return checkPermission(Action.CREATE, target);
    }

    public boolean canUpdate(String target) {
        if (role.getIsSuperUser()) {
            return true;
        }
        return checkPermission(Action.UPDATE, target);
    }

    public boolean canDelete(String target) {
        if (role.getIsSuperUser()) {
            return true;
        }
        return checkPermission(Action.DELETE, target);
    }

    public boolean canView(String target) {
        if (role.getIsSuperUser()) {
            return true;
        }
        return checkPermission(Action.READ, target);
    }
    

    /**
     * Checks if the entity is owned by the role.
     * 
     * @param entity
     * @return
     */
    public boolean isOwner(HasRole entity) {
        if(entity.getRole() == null) {
            logger.error("Role is missing.");
            return false;
        }

        return entity.getRole().equals(role);
    }

    public Role getRole() {
       return role; 
    }

    private EntityManager getEm() {
        return entityManagerProvider.get();
    }

    public void logout() {
//        SecurityUtils.getSubject().logout();
        role = anonymousRole;
    }

}

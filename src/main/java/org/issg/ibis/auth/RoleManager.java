package org.issg.ibis.auth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jrc.edit.Dao;
import org.jrc.edit.HasRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.auth.domain.Role;
import org.vaadin.addons.auth.domain.Role_;
import org.vaadin.addons.oauth.OAuthSubject;
import org.vaadin.addons.oauth.UserInfo;

import com.google.inject.Inject;
import com.google.inject.servlet.SessionScoped;

@SessionScoped
public class RoleManager implements OAuthSubject {

    public enum Action {
        READ, CREATE, UPDATE, DELETE
    }

    Logger logger = LoggerFactory.getLogger(RoleManager.class);

    private Set<String> stringPermissions = new HashSet<String>();
    
    private Role role;

    private Dao dao;

    private Role anonymousRole;

	private UserInfo userInfo;

	private Set<Action> allActions;
    
    @Inject
    public RoleManager(Dao dao) {

        this.dao = dao;

        this.anonymousRole = new Role();
        
        role = anonymousRole;

        stringPermissions = role.getStringPermissions();

        allActions = new HashSet<Action>(Arrays.asList(Action.values()));
    }
    
    public Set<Action> getActionsForTarget(Class<?> clazz) {
    	
        if (role.getIsSuperUser()) {
            return allActions;
        }
        
        HashSet<Action> actions = new HashSet<RoleManager.Action>();
        Action[] values = Action.values();
		for (int i = 0; i < values.length; i++) {
			Action action = values[i];
			boolean hasPermission = stringPermissions.contains(action + "_" + clazz.getSimpleName());
			if (hasPermission) {
				actions.add(action);
			}
		}
		return actions;
        
    }

    @Deprecated
    /**
     * use #
     */
    public boolean checkPermission(Action action, String target) {
        
        if (role.getIsSuperUser()) {
            return true;
        }
        
		boolean hasPermission = stringPermissions.contains(action + "_" + target);

        logger.debug(action + "_" + target);
        logger.debug("has permission: " + hasPermission);
        return hasPermission;

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


    /**
     * Sets the user to be anonymous and returns to beginning
     */
    public void logout() {
        role = anonymousRole;
    }

	@Override
	public String getEmail() {
		return role.getEmail();
	}

	@Override
	public Long getUserPrincipal() {
		return role.getId();
	}

	@Override
	public void setUserInfo(UserInfo userInfo) {

		this.userInfo = userInfo;

		if (!userInfo.getVerifiedEmail()) {
			logger.info("Unverified user email attempt");
			return;
		}

		Role r = dao.findByProxyId(Role_.email, userInfo.getEmail());
		if (r != null) {
			role = r;
		} else {
//			createAccount!
			//Can we just manage with the email and nothing else?
			Role newRole = new Role();
			newRole.setEmail(userInfo.getEmail());
			newRole.setFirstName(userInfo.getGivenName());
			newRole.setLastName(userInfo.getFamilyName());
			dao.persist(newRole);	
			role = newRole;
		}
		
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public boolean isLoggedIn() {
		return role.getId() != null && role.getId() > 0;
	}

}
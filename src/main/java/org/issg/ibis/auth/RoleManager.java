package org.issg.ibis.auth;

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
    
    @Inject
    public RoleManager(Dao dao) {

        this.dao = dao;

        this.anonymousRole = new Role();
        
        role = anonymousRole;

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
			//FIXME keep them anonymous
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
		}
		
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public boolean isLoggedIn() {
		return role.getId() != null && role.getId() > 0;
	}

}

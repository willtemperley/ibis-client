package org.issg.ibis.auth;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.auth.domain.Role;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * @author Will Temperley
 * 
 *         Essentially a DAO that supplies login and credential information to
 *         Shiro.
 * 
 */
@Singleton
public class JpaRealm  {


    Logger logger = LoggerFactory.getLogger(JpaRealm.class);
    private EntityManager entityManager;

    @Inject
    public JpaRealm(EntityManagerFactory emf) {
        this.entityManager = emf.createEntityManager();
    }

    /**
     * Gets a role by its identity. If this fails, the email address is tried,
     * as this is used to retrieve Google openIds.
     */
    void getUserInfo() {
    	//FIXME
        
        /*
         * Obtain the account details
         */
        TypedQuery<Role> q = entityManager.createNamedQuery("Role.findRoleByIdentity", Role.class);
//        q.setParameter("identity", token.getUsername());

        List<Role> roles = q.getResultList();

        if (roles.size() == 0) {
            q = entityManager.createNamedQuery("Role.findRoleByEmail",
                    Role.class);
            //FIXME
            Object email = "";
			q.setParameter("email", email);
            roles = q.getResultList();
        }

        /*
         * Get the first role in the list. There must be at least one role here.
         * Throw exception if account is locked.
         */
        Role role = roles.get(0);

        /*
         * Ensure the role is completely reloaded from the database, to ensure
         * any changes made externally, e.g. permission changes have been
         * reloaded properly.
         */
        entityManager.refresh(role);
        if (role.getCanLogin()) {
//            return new SimpleAuthenticationInfo(role.getId(), "", getName());
        } else {
//            throw new LockedAccountException();
        }

    }


}
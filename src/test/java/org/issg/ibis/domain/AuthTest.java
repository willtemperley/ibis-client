package org.issg.ibis.domain;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.auth.RoleManager.Action;
import org.jrc.edit.Dao;
import org.junit.Test;
import org.vaadin.addons.auth.domain.Role;

import com.google.inject.Provider;



public class AuthTest {
	
	@Test
	public void testAuth() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ibis-domain");
        EntityManager em = emf.createEntityManager();
        
        
        
        Role r = em.find(Role.class, 57l);
        System.out.println(r.getEmail());
        
        
        System.out.println(r.getStringPermissions());
        
        RoleManager rm = new RoleManager(new Dao(new Provider<EntityManager>() {
			
			@Override
			public EntityManager get() {
				return emf.createEntityManager();
				// TODO Auto-generated method stub
			}
		}, emf, null), "false");
        
        rm.setRole(r);
        
        Set<Action> a = rm.getActionsForTarget(Species.class);
        
        for (Action action : a) {
        	System.out.println(a);
		}
	}



}

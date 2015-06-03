package org.biopama.ibis.domain;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.issg.ibis.domain.Location;
import org.issg.ibis.domain.SpeciesImpact;
import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;


public class BootstrapDomain {
    
    @Test
    public void bootDomain() {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ibis-domain");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<SpeciesImpact> q = em.createQuery("from SpeciesImpact", SpeciesImpact.class);
        List<SpeciesImpact> results = q.getResultList();
        for (SpeciesImpact speciesImpact : results) {
            System.out.println(speciesImpact);
        }
//        TypedQuery<Species> q = em.createQuery("from Species", Species.class);
//        List<Species> results = q.getResultList();
//        for (Species species : results) {
//            System.out.println(species);
//            System.out.println(species.getSpeciesImpacts());
//        }
        
    }

	@Test
	public void locationGeomLazy() {
	        
	        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ibis-domain");
	        EntityManager em = emf.createEntityManager();
	        
	        TypedQuery<Location> q = em.createQuery("from Location", Location.class);
	        List<Location> results = q.getResultList();
	        for (Location location : results) {
	            System.out.println(location);
	            
	            em.detach(location);

	            Geometry g = location.getGeom();
	        }
	//        TypedQuery<Species> q = em.createQuery("from Species", Species.class);
	//        List<Species> results = q.getResultList();
	//        for (Species species : results) {
	//            System.out.println(species);
	//            System.out.println(species.getLocations());
	//        }
	        
	    }

}

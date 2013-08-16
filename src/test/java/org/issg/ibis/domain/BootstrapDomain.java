package org.issg.ibis.domain;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;


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

}

package org.issg.ibis.client.pending;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.PluralAttribute;

import org.issg.ibis.domain.view.ResourceDescription;
import org.issg.ibis.domain.view.ResourceDescriptionSpecies;
import org.issg.ibis.domain.view.ResourceDescriptionSpecies_;
import org.issg.ibis.domain.view.ResourceDescription_;
import org.jrc.persist.Dao;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;

/**
 * @author Will Temperley
 *
 * @param <T>
 */
public class CriteriaQueryManager<T> {

    class CorrelatedSubqueryIngredients {
    
        public Object memberOfCollection;
    
        public PluralAttribute<T, ?, ?> pluralAttribute;
        
    }

    private Dao dao;
    
//    private Set<CorrelatedSubqueryIngredients> csis = new HashSet<CorrelatedSubqueryIngredients>();
    
    private int pageSize;
    
    private Map<String, CorrelatedSubqueryIngredients> csiMap = new HashMap<String, CorrelatedSubqueryIngredients>();

    public CriteriaQueryManager(Dao dao, int pageSize) {
        
        this.dao = dao;
        this.pageSize = pageSize;

    }
    
    public List<ResourceDescription> getResourceDescriptions() {

        EntityManager em = dao.getEntityManager();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<ResourceDescription> query = criteriaBuilder
                .createQuery(ResourceDescription.class);
        Root<ResourceDescription> person = query
                .from(ResourceDescription.class);

        // Main query
        query.select(person);
        
        addFilters(criteriaBuilder, query);
        
        query.orderBy(criteriaBuilder.desc(person.get(ResourceDescription_.impactCount)));


        // 
        TypedQuery<ResourceDescription> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }


    public <X, C extends Collection<X>> void addExistsPredicate(
            PluralAttribute<T, C, X> attr, X memberOfCollection) {
        
        // null means we need not query on this attribute any more
        if (memberOfCollection == null) {
            csiMap.remove(attr.getName());
            return;
        }
        
        // Because we don't have the query when the filters are being added, need to store the pre-requisites
        CorrelatedSubqueryIngredients csi = new CorrelatedSubqueryIngredients();
        csi.pluralAttribute = attr;
        csi.memberOfCollection = memberOfCollection;
//        csis.add(csi);
        csiMap.put(attr.getName(), csi);
        
    };
    
    public void addFilters(CriteriaBuilder cb, CriteriaQuery<?> query) {

        Set<String> keys = csiMap.keySet();
        Predicate[] arr = new Predicate[csiMap.size()];

//        int i = 0;
//        for (CorrelatedSubqueryIngredients csi : csis) {
//            Predicate exists = getCorreleatedSubQuery(query, cb, csi.memberOfCollection);
//            arr[i] = exists;
//            i++;
//        }

        int i = 0;
        for (String string : keys) {
            CorrelatedSubqueryIngredients csi = csiMap.get(string);

//            if (csi.pluralAttribute.equals(ResourceDescription_.species)) {
                Predicate exists = getCorreleatedSubQuery(query, cb, csi);
                arr[i] = exists;
                i++;
//            }

        }

        query.where(arr);

    }

    private Root<T> getRoot(CriteriaQuery<?> query) {
            return (Root<T>) query.getRoots().iterator().next();
    }

    public Predicate getCorreleatedSubQuery(CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, CorrelatedSubqueryIngredients csi) {

        Root<T> root = getRoot(query);

        // Subquery on entity which references main entity through a m:1
        Subquery<ResourceDescriptionSpecies> subquery = query.subquery(ResourceDescriptionSpecies.class);
        Root<ResourceDescriptionSpecies> subRootEntity = subquery.from(ResourceDescriptionSpecies.class);
        subquery.select(subRootEntity);

        //Match between the subquery and query
        Predicate correlatePredicate = criteriaBuilder.equal(subRootEntity
                .get(ResourceDescriptionSpecies_.resourceDescription), root);

        //Match inside the subquery
        Predicate relatedObjectPredicate = criteriaBuilder.equal(
                subRootEntity.get(ResourceDescriptionSpecies_.species), csi.memberOfCollection); // The species

        subquery.where(correlatePredicate, relatedObjectPredicate);

        Predicate pred = criteriaBuilder.exists(subquery);
        
        return pred;
    }

}

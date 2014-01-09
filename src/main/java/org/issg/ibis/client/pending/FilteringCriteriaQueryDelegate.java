package org.issg.ibis.client.pending;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.PluralAttribute;

import org.issg.ibis.domain.view.ResourceDescriptionSpecies;
import org.issg.ibis.domain.view.ResourceDescriptionSpecies_;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;

/**
 * 
 * 
 * @author Will Temperley
 *
 * @param <T>
 */
public class FilteringCriteriaQueryDelegate<T> extends
        DefaultQueryModifierDelegate {

    class CorrelatedSubqueryIngredients {
    
        public Object memberOfCollection;
    
        public PluralAttribute<T, ?, ?> pluralAttribute;
        
    }

    private Set<CorrelatedSubqueryIngredients> csis = new HashSet<CorrelatedSubqueryIngredients>();

    public <X, C extends Collection<X>> void addExistsPredicate(
            PluralAttribute<T, C, X> attr, X memberOfCollection) {
        
        // Because we don't have the query when the filters are being added, need to store the pre-requisites
        CorrelatedSubqueryIngredients csi = new CorrelatedSubqueryIngredients();
        csi.pluralAttribute = attr;
        csi.memberOfCollection = memberOfCollection;
        csis.add(csi);
        
    };
    
    @Override
    public void filtersWillBeAdded(CriteriaBuilder cb, CriteriaQuery<?> query,
            List<Predicate> predicates) {

        for (CorrelatedSubqueryIngredients csi : csis) {

            Predicate exists = getCorreleatedSubQuery(query, cb, csi.memberOfCollection);
            predicates.add(exists);
            
        }

        super.filtersWillBeAdded(cb, query, predicates);
    }


    private Root<T> getRoot(CriteriaQuery<?> query) {
            return (Root<T>) query.getRoots().iterator().next();
    }

    public Predicate getCorreleatedSubQuery(CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, Object s) {


        Root<T> root = getRoot(query);

        // Subquery on entity which references main entity through a m:1
        Subquery<ResourceDescriptionSpecies> subquery = query.subquery(ResourceDescriptionSpecies.class);
        Root<ResourceDescriptionSpecies> subRootEntity = subquery.from(ResourceDescriptionSpecies.class);
        subquery.select(subRootEntity);

        Predicate correlatePredicate = criteriaBuilder.equal(subRootEntity
                .get(ResourceDescriptionSpecies_.resourceDescription), root);

        Predicate relatedObjectPredicate = criteriaBuilder.equal(
                subRootEntity.get(ResourceDescriptionSpecies_.species), s); // The species

        subquery.where(correlatePredicate, relatedObjectPredicate);

        Predicate pred = criteriaBuilder.exists(subquery);
        
        return pred;

    }

}

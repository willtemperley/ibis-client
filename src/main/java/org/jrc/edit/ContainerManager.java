package org.jrc.edit;


import org.issg.ibis.auth.RoleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.maddon.ListContainer;


/**
 * Manages container construction and entity persistence
 * 
 * @author Will Temperley
 * 
 */
public class ContainerManager<T> {

    private Dao dao;
    private ListContainer<T> container;

    private Class<T> clazz;
    
    private boolean canDelete;
    private boolean canUpdate;
    private boolean canCreate;
    
    private Logger logger = LoggerFactory.getLogger(ContainerManager.class);

    public ContainerManager(Dao dao, Class<T> clazz, RoleManager roleManager) {

        this.dao = dao;
        this.clazz = clazz;

        container = new ListContainer<T>(clazz);
        

        
        String target = roleManager.getUrlForClass(clazz);
        
        logger.debug("target: "+ target);
        
        canCreate = roleManager.canCreate(target);
        canUpdate = roleManager.canUpdate(target);
        canDelete = roleManager.canDelete(target);

    }

    public ListContainer<T> getContainer() {
        return container;
    }

    public T newEntity() {
        try {
            T entity =  clazz.newInstance();
            return entity;
            
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds the entity to the container and commits, returning the ID.
     * 
     * @param entity
     * @return
     */
    public Object addEntity(T entity) {
        Object id = container.addItem(entity);
        return id;
    }

    public void deleteEntity(T entity) {
        /*
         * Delete happens through finding the id then calling remove on the
         * container
         * 
         * TODO: this should be part of the Vaadin library, surely? Difficult to
         * understand why they don't follow the EntityManager api and work-flow
         * more closely.
         */
        Object id = dao.getId(entity);
//        System.out.println("Deleting: " + entity);
        container.removeItem(id);
//        container.commit();
    }

    public Object getId(Object entity) {
        return dao.getId(entity);
    }

    public boolean canDelete() {
        return canDelete;
    }
    
    public boolean canUpdate() {
        return canUpdate;
    }
    
    public boolean canCreate() {
        return canCreate;
    }

	public T findEntity(Object id) {
		return dao.find(clazz, id);
	}

}

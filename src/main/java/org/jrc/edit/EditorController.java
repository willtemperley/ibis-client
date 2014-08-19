package org.jrc.edit;

import java.util.List;
import java.util.Set;

import org.issg.ibis.auth.RoleManager;
import org.issg.ibis.auth.RoleManager.Action;
import org.issg.ibis.editor.view.IEditorView;
import org.issg.ibis.responsive.TakesSelectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.form.field.FieldGroup;
import org.vaadin.addons.form.field.FieldGroupManager;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.ListContainer;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;

import com.google.common.base.Joiner;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

/**
 * 
 * A simplified verion of {@link BaseEditor}
 * 
 * Probably do not require all the container stuff.
 * 
 */
public class EditorController<T> {

	private static final String SAVE_MESSAGE = "Saved successfully.";

	private Logger logger = LoggerFactory.getLogger(EditorController.class);

	protected Dao dao;

	private FieldGroupManager<T> fgm = new FieldGroupManager<T>();

	/*
	 * The field factory
	 */
	protected JpaFieldFactory<T> ff;


	private RoleManager roleManager;

	private Class<T> clazz;

	private ListContainer<T> container;

	private IEditorView<T> view;

	public interface EditCompleteListener<T> {

		public void onEditComplete(T entity);

	}

	public EditorController(final Class<T> clazz, final Dao dao,
			RoleManager roleManager) {

		this.clazz = clazz;

		this.dao = dao;

		this.roleManager = roleManager;

		this.ff = new JpaFieldFactory<T>(dao, clazz);

		this.container = new ListContainer<T>(clazz);

	}

	public ListContainer<T> getContainer() {
		return container;
	}

	public FieldGroup<T> addFieldGroup(String name) {
		FieldGroup<T> fieldGroup = ff.getFieldGroup(name);
		fgm.add(fieldGroup);
		return fieldGroup;
	}

	/**
	 * Designed to be overridden to allow customised form construction.
	 * 
	 * @param editorView
	 */
	public void init(IEditorView<T> editorView) {

		this.view = editorView;
		
		if (fgm.getFieldGroups().isEmpty()) {
			addFieldGroup("");
		}

		editorView.buildForm(fgm.getFieldGroups());

		/*
		 * Submit panel
		 */

		editorView.setCreateListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				view.setIsEditing(true);
				doCreate();
			}
		});

		editorView.setUpdateListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				commitForm(true);
			}
		});

		editorView.setCancelListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				view.setIsEditing(false);
			}
		});

		Set<Action> allowedActions = roleManager.getActionsForTarget(clazz);
		editorView.setAllowedActions(allowedActions);

		editorView.setDeleteListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				ConfirmDialog.show(UI.getCurrent(),
						"Are you sure you wish to delete this record?",
						new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									doDelete();
									view.setIsEditing(false);
								}
							}

						});
			}
		});

	}

	/**
	 * The field factory is a custom component for each editor. For polymorphic
	 * entities, the field factory is passed the object to allow it to determine
	 * the correct subclass.
	 * 
	 * @return the custom field factory
	 */
	public JpaFieldFactory<T> getFieldFactory() {
		return ff;
	}

	protected boolean commitForm(boolean showNotification) {

		T entity = fgm.getEntity();

		boolean x = isFormValid();

		if (x == false) {
			if (showNotification) {
				List<String> messages = fgm.getValidationMessages();

				String message = Joiner.on("\n").join(messages);
				Notification.show("Validation failed", message,
						Type.ERROR_MESSAGE);
			}

			return false;
		}

		try {
			fgm.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		/*
		 * Subclasses may define tasks to perform pre commit.
		 */
		doPreCommit(entity);

		container.addItem(entity);

		if (showNotification) {
			Notification.show(SAVE_MESSAGE);
		}

		Object id = dao.getId(entity);
		if (id == null) {
			dao.persist(entity);
		} else {
			dao.merge(entity);
		}
		// entity = containerManager.findEntity(id);

		/*
		 * Makes sure the bean is in sync with the field group. Especially
		 * important when data are generated by the database (serial IDs,
		 * triggers etc).
		 */
		fgm.setEntity(entity);

		/*
		 * Subclasses may define tasks to perform post-commit.
		 */
		doPostCommit(entity);

		return true;
	}

	protected boolean isFormValid() {
		boolean x = fgm.isValid();
		return x;
	}

	public void doCreate() {
		try {
			fgm.setEntity(clazz.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Optionally register a selection component here.
	 * 
	 * @param selector
	 */
	public void setSelectionComponent(TakesSelectionListener<T> selector) {
		selector.addMValueChangeListener(new MValueChangeListener<T>() {
			@Override
			public void valueChange(MValueChangeEvent<T> event) {
				T val = event.getValue();
				if (val != null) {
					doUpdate(val);
				}
			}
		});
	}

	public void doUpdateById(Object id) {
		T entity = dao.get().find(clazz, id);
		if (entity == null) {
			return;
		}
		fgm.setEntity(entity);
		view.setIsEditing(true);
	}

	public void doUpdate(T entity) {
		Object id = dao.getId(entity);
		entity = dao.get().find(clazz, id);
		Page currentPage = Page.getCurrent();
		String frag = currentPage.getUriFragment();

		/*
		 * Add or replace the identifier
		 */
		if (frag.contains("/")) {
			frag = frag.split("/")[0] + "/" + id;
		} else {
			frag = frag + "/" + id;
		}
		currentPage.setUriFragment(frag);
		fgm.setEntity(entity);
		view.setIsEditing(true);
	}

	private void doDelete() {
		T entity = fgm.getEntity();
		if (entity == null) {
			logger.error("Delete attempted with null entity.");
			return;
		}
		Object id = dao.getId(entity);
		if (id == null) {
			logger.error("This entity does not yet exist.");
			return;
		}
		dao.remove(clazz, id);

		container.removeItem(entity);
		doPostDelete(entity);
	}

	/**
	 * Called after deletion for any cleanup that may be required.
	 * 
	 * @param entity
	 */
	protected void doPostDelete(T entity) {
	}

	/**
	 * Can be overridden by classes that require parent child associations to be
	 * fixed.
	 */
	protected void doPreCommit(T entity) {
	}

	/**
	 * Subclasses can use this method for obtaining notification of commit
	 * actions.
	 * 
	 * @param entity
	 */
	protected void doPostCommit(T entity) {
		if (entity == null) {
			logger.error("Entity is null");
		}
//		view.setIsEditing(false);
	}

	public T getEntity() {
		return fgm.getEntity();
	}
	
	public void setEntity(T entity) {
		fgm.setEntity(entity);
	}

	protected List<FieldGroup<T>> getFieldGroups() {
		return fgm.getFieldGroups();
	}

	public JpaFieldFactory<T> getFf() {
		return ff;
	}

	public boolean hasReadPermission() {

		Set<Action> actions = roleManager.getActionsForTarget(clazz);
		if (actions != null && actions.contains(Action.READ)) {
			return true;
		}
		return false;
		
	}
}

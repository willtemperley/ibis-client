package org.jrc.edit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.vaadin.addons.form.field.ColorField;
import org.vaadin.addons.form.field.FieldGroup;
import org.vaadin.addons.form.util.AdminStringUtil;

import com.mysema.query.types.Path;
import com.mysema.query.types.path.SetPath;
import com.mysema.query.types.path.StringPath;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;

/**
 * Generates field groups based on {@link StaticMetamodel} properties. This
 * provides a type-safe method of creating forms.
 * 
 * Most fields can be created with the addField(prop) method, however extra
 * methods have been provided for differentiating between various types of text
 * areas.
 * 
 * Arbitrary fields can be added using the addField method.
 * 
 * @author Will Temperley
 * 
 * @param <T>
 * 
 */
public class JpaFieldFactory<T> {

    private static final String NULL_REPRESENTATION = "";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_FIELD_WIDTH = "300px";

    private List<String> order = new ArrayList<String>();
//    private List<String> disabledFields = new ArrayList<String>();

    private Map<String, Field<?>> fieldBuffer = new HashMap<String, Field<?>>();
    private Dao dao;
    private Class<T> clazz;

    // private ContainerManager<T> containerManager;

    public JpaFieldFactory(Dao dao, Class<T> clazz) {
        this.dao = dao;
        this.clazz = clazz;
    }

/**
     * Adds a field to the temporary cache. Ensures order is also set.
     * 
     * @param prop
     *            the metamodel property
     * @param f
     *            the field
     */
    public void addField(Attribute<? extends T, ?> prop, Field<?> f) {

        String propName = prop.getName();

//        String fieldDescription = dao.getFieldDescription(prop);

        /**
         * setDescription removed from Field interface in 7, therefore we have
         * to do this separately for every field type, or more simply up-cast
         * like here.
         */
//        if (f instanceof AbstractField<?> && fieldDescription != null) {
//            ((AbstractField<?>) f).setDescription(fieldDescription);
//        }

        f.setCaption(AdminStringUtil.splitCamelCase(propName));

        fieldBuffer.put(propName, f);
        order.add(propName);
    }
    
    public void addQField(Path<?> path, Field<?> f) {
    	String propName = path.getMetadata().getName();
        f.setCaption(AdminStringUtil.splitCamelCase(propName));

        fieldBuffer.put(propName, f);
        order.add(propName);
    }


    public TextField addQTextField(Path<?> path) {
        TextField f = new TextField();
        f.setNullRepresentation(NULL_REPRESENTATION);
        f.setWidth(DEFAULT_FIELD_WIDTH);
        addQField(path, f);
        return f;
    }

    public void addColorField(String propName) {
        ColorField f = new ColorField();

        f.setCaption(AdminStringUtil.splitCamelCase(propName));

        fieldBuffer.put(propName, f);
        order.add(propName);
    }

    



//    public <X> SelectOrCreateField<X> addSelectAndCreateField(
//            Attribute<? extends T, X> prop,
//            SingularAttribute<X, ?>... childProps) {
//
//        SelectOrCreateField<X> socf = new SelectOrCreateField<X>(
//                prop.getJavaType(), dao);
//
//        for (int i = 0; i < childProps.length; i++) {
//            socf.addField(childProps[i]);
//        }
//        addField(prop, socf);
//        socf.init();
//        return socf;
//
//    }

    private TwinColSelect addQM2MField(SetPath<?, ?> path) {

        TwinColSelect l = new TwinColSelect(AdminStringUtil.splitCamelCase(path
                .getMetadata().getName()));
        l.setWidth("25em");

        /*
         * Get the type the attribute contains
         */
        Class<?> clazz = path.getElementType();

        List<?> objects = dao.all(clazz);
        for (Object object : objects) {
            l.addItem(object);
        }

        l.setMultiSelect(true);

        addQField(path, l);
        return l;
    }

    // FIXME copy (overload of above) for quick fix
    // Lost info
    public <X> TwinColSelect addM2MField(Attribute<? extends T, ?> prop,
            SingularAttribute<?, ?> orderBy) {

        TwinColSelect l = new TwinColSelect(AdminStringUtil.splitCamelCase(prop
                .getName()));
        l.setWidth("25em");

        /*
         * Get the type the attribute contains
         */
        Class<T> clazz = PluralAttribute.class.cast(prop).getElementType()
                .getJavaType();

        List<T> objects = dao.all(clazz, orderBy);

        for (Object object : objects) {
            l.addItem(object);
        }

        l.setMultiSelect(true);

        addField(prop, l);
        return l;
    }

    /**
     * @return the order in which the fields should be displayed on the form.
     *         This is the same as the order they were added.
     */
    public List<String> getOrder() {
        return order;
    }

    //    public <X> void setFieldValue(Attribute<? extends T, X> prop, X value) {
//        fields.g
//    }

    /**
     * Using a string for the property is useful for transient fields, where the
     * {@link StaticMetamodel} doesn't work.
     * 
     * @param propName
     * @param f
     */
    public void addField(String propName, Field<?> f) {
        fieldBuffer.put(propName, f);
        order.add(propName);
    }

    /**
     * This works but is very hacky
     * 
     * @return
     */
    @Deprecated
    public FieldGroup<T> getFieldGroup(String name) {
        

        BeanFieldGroup<T> bfg = new BeanFieldGroup<T>(clazz);

        for (String propName : order) {
            Field<?> f = fieldBuffer.get(propName);
            if (f != null) {
                bfg.bind(f, propName);
            }
        }

        //FIXME Description from database??
        FieldGroup<T> fg = new FieldGroup<T>(bfg, name, null);

        fieldBuffer.clear();
        return fg;
    }
    
    public <X> Component addQField(Path<X> path) {
    	
    	Class<? extends X> clazz = path.getType();
    	
        if (Enum.class.isAssignableFrom(clazz)) {
        	ComboBox cb = new ComboBox();
        	Object[] consts = path.getType().getEnumConstants();
        	for (int i = 0; i < consts.length; i++) {
        		cb.addItem(consts[i]);
        	}
        	addQField(path, cb);
        	return cb;
        } else if (clazz.equals(String.class)) {
            return addQTextField(path);
        } else if (Number.class.isAssignableFrom(clazz)) {
            return addQTextField(path);
        } else if (clazz.equals(Boolean.class)) {
            CheckBox checkBox = new CheckBox();
            addQField(path, checkBox);
            return checkBox;
        } else if (clazz.equals(Date.class)) {
            DateField dateField = new DateField();
            dateField.setDateFormat(DEFAULT_DATE_FORMAT);
            addQField(path, dateField);
        return dateField;
        } else if (clazz.getAnnotation(Entity.class) != null) {
        	ComboBox cb = new ComboBox();
        	cb.setWidth(DEFAULT_FIELD_WIDTH);
        	List<?> list = dao.all(path.getType());
        	for (Object x : list) {
        		cb.addItem(x);
        	}
        	addQField(path, cb);
        	return cb;
        } else if (path instanceof SetPath) {
            return addQM2MField((SetPath) path);
        }
		return null;
    }

	public TextArea addQTextArea(StringPath path) {
        TextArea f = new TextArea();
        f.setNullRepresentation(NULL_REPRESENTATION);
        f.setWidth(DEFAULT_FIELD_WIDTH);
        addQField(path, f);
        return f;
	}

	public AbstractComponent addQRichTextArea(StringPath content) {
        RichTextArea f = new RichTextArea();
        f.setNullRepresentation(NULL_REPRESENTATION);
        f.setWidth(DEFAULT_FIELD_WIDTH);
        addQField(content, f);
		return f;
	}

}
package org.biopama.ibis.editor.basic;

import com.google.inject.Inject;

import org.biopama.edit.Dao;
import org.biopama.ibis.auth.RoleManager;
import org.issg.ibis.domain.QReference;
import org.issg.ibis.domain.QSiteContent;
import org.issg.ibis.domain.Reference;
import org.issg.ibis.domain.SiteContent;

public class SiteContentEditor extends BasicTwinPanelEditor<SiteContent> {

    @Inject
    public SiteContentEditor(Dao dao, RoleManager roleManager) {

    	super(SiteContent.class, dao, roleManager);

        QSiteContent entityPath = QSiteContent.siteContent;
        ff.addQField(entityPath.contentType);
        ff.addQField(entityPath.title);
		ff.addQRichTextArea(entityPath.content);

        build(entityPath);
        
        selector.addColumns("id");
    }

}

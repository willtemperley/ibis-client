package org.issg.ibis.editor;

import org.issg.ibis.domain.Issue;
import org.issg.ibis.domain.Issue_;
import org.jrc.form.editor.ui.SimpleTwinPanelEditor;
import org.jrc.persist.Dao;

import com.google.inject.Inject;

public class IssueEditor extends
        SimpleTwinPanelEditor<Issue> {


    @Inject
    public IssueEditor(Dao dao) {
       super(Issue.class, dao);

       getTable().addColumns(Issue_.title, Issue_.role);

       filterPanel.addFilterField(Issue_.title);

         
        ff.addField(Issue_.title);
        ff.addField(Issue_.content);
        ff.addField(Issue_.links);
        ff.addField(Issue_.role);

        init();

    }

}

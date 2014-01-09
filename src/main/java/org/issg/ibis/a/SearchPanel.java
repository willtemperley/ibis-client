package org.issg.ibis.a;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.issg.ibis.client.pending.FilteringCriteriaQueryDelegate;
import org.issg.ibis.domain.Species;
import org.issg.ibis.domain.view.ResourceDescription;
import org.issg.ibis.domain.view.ResourceDescription_;
import org.jrc.form.editor.EntityTable;
import org.jrc.form.filter.FilterPanel;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;
import org.jrc.ui.SimpleHtmlLabel;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class SearchPanel extends VerticalLayout {

    private JPAContainer<ResourceDescription> esiContainer;

    private Dao dao;

    private SearchSelectEventListener searchListener;

    public SearchPanel(Dao dao) {

        addStyleName("layout-panel");

        ContainerManager<ResourceDescription> containerManager = new ContainerManager<ResourceDescription>(
                dao, ResourceDescription.class, true);
        this.esiContainer = containerManager.getContainer();

        {

            FilteringCriteriaQueryDelegate<ResourceDescription> queryModifierDelegate = new FilteringCriteriaQueryDelegate<ResourceDescription>();

            TypedQuery<Species> q = dao.getEntityManager().createNamedQuery(
                    Species.INVASIVE, Species.class);
            Species testobj = q.getResultList().get(0);

            System.out.println(testobj);

            queryModifierDelegate.addExistsPredicate(ResourceDescription_.invasiveSpecies, testobj);

            esiContainer.setQueryModifierDelegate(queryModifierDelegate);

        }

        this.dao = dao;

        {
            setSizeFull();
            addComponent(getFilterPanel());
            addComponent(getSearchEntityTable());

        }

        esiContainer.sort(new String[] { "impactCount" },
                new boolean[] { false });

    }

    private FilterPanel<ResourceDescription> getFilterPanel() {

        FilterPanel<ResourceDescription> fp = new FilterPanel<ResourceDescription>(
                esiContainer, dao);

        fp.addFilterField(ResourceDescription_.name);

        // fp.addFilterField(SearchEntity_.);
        // fp.addFilterField(SearchEntity_.designatedAreaType);
        // fp.addFilterField(SearchEntity_.);

        return fp;

    }

    class ImpactVisualizationColumn implements Table.ColumnGenerator {

        public Component generateCell(Table source, Object itemId,
                Object columnId) {
            JPAContainerItem<?> item = (JPAContainerItem<?>) source
                    .getItem(itemId);
            final ResourceDescription si = (ResourceDescription) item.getEntity();
            return new SearchResult(si);
        }
    }

    public class SearchResult extends Panel {

        public SearchResult(ResourceDescription facetedSearch) {

            // ResourceType rt = facetedSearch.getResourceType();
            char initial = facetedSearch.getId().charAt(0);

            String html = "<div class='search-result result-type-" + initial
                    + "'>" + initial + "</div>";
            html += getLink(facetedSearch);

            html += ("<div>Known impacts: " + facetedSearch.getImpactCount() + "</div>");

            SimpleHtmlLabel shl = new SimpleHtmlLabel(html);

            setContent(shl);
        }

        private String getLink(ResourceDescription esi) {
            StringBuilder sb = new StringBuilder(
                    "<div class='search-description'><a href='#!");
            sb.append(esi.getId());
            sb.append("'>");
            sb.append(esi.toString());
            sb.append("</a></div>");
            return sb.toString();
        }
    }

    private EntityTable<ResourceDescription> getSearchEntityTable() {

        EntityTable<ResourceDescription> table = new EntityTable<ResourceDescription>(
                esiContainer);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

        table.setHeight("100%");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Object value = event.getProperty().getValue();
                EntityItem<ResourceDescription> obj = esiContainer.getItem(value);
                if (value == null) {
                    return;
                }
                String si = (String) value;
                entitySelected(obj.getEntity());

            }
        });

        ImpactVisualizationColumn generatedColumn = new ImpactVisualizationColumn();
        table.addGeneratedColumn("id", generatedColumn);
        table.setColumnWidth("id", 400);
        // table.addColumns(SearchEntity_.name);
        return table;

    }

    protected void entitySelected(ResourceDescription facetedSearch) {
        if (searchListener != null) {
            searchListener.onSelect(facetedSearch);
        }
    }

    public void addSearchListener(
            SearchSelectEventListener searchSelectEventListener) {
        this.searchListener = searchSelectEventListener;
    }
}

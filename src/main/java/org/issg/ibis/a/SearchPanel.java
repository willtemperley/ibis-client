package org.issg.ibis.a;

import org.issg.ibis.a.event.SearchSelectEventListener;
import org.issg.ibis.domain.FacetedSearch;
import org.issg.ibis.domain.FacetedSearch_;
import org.issg.ibis.domain.ResourceType;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class SearchPanel extends HorizontalLayout {

    private JPAContainer<FacetedSearch> esiContainer;

    private Dao dao;

    private SearchSelectEventListener searchListener;

    public SearchPanel(Dao dao) {
        
        addStyleName("layout-panel");
        
        ContainerManager<FacetedSearch> containerManager = new ContainerManager<FacetedSearch>(
                dao, FacetedSearch.class, true);
        this.esiContainer = containerManager.getContainer();
        this.dao = dao;

        {
            setSizeFull();
            addComponent(getFilterPanel());
            addComponent(getFacetedSearchTable());

        }
        
        esiContainer.sort(new String[]{"impactCount"}, new boolean[]{false});

    }

    private FilterPanel<FacetedSearch> getFilterPanel() {

        FilterPanel<FacetedSearch> fp = new FilterPanel<FacetedSearch>(
                esiContainer, dao);

        fp.addFilterField(FacetedSearch_.name);
        fp.addFilterField(FacetedSearch_.country);
        fp.addFilterField(FacetedSearch_.resourceType);
        // fp.addFilterField(FacetedSearch_.);
//        fp.addFilterField(FacetedSearch_.designatedAreaType);
        // fp.addFilterField(FacetedSearch_.);
        return fp;

    }

    class ImpactVisualizationColumn implements Table.ColumnGenerator {

        public Component generateCell(Table source, Object itemId,
                Object columnId) {
            JPAContainerItem<?> item = (JPAContainerItem<?>) source
                    .getItem(itemId);
            final FacetedSearch si = (FacetedSearch) item.getEntity();
            return new SearchResult(si);
        }
    }

    public class SearchResult extends Panel {

        public SearchResult(FacetedSearch facetedSearch) {

            ResourceType rt = facetedSearch.getResourceType();
            
            String html = "<div class='search-result result-type-" + rt.getId() + "'>" + rt.getId() + "</div>";
            html += getLink(facetedSearch);
            
            html += ("<div>Known impacts: " + facetedSearch.getImpactCount() + "</div>");
            
            SimpleHtmlLabel shl = new SimpleHtmlLabel(html);

            setContent(shl);
        }

        private String getLink(FacetedSearch esi) {
            StringBuilder sb = new StringBuilder("<div class='search-description'><a href='#!");
            sb.append(esi.getId());
            sb.append("'>");
            sb.append(esi.toString());
            sb.append("</a></div>");
            return sb.toString();
        }
    }


    private EntityTable<FacetedSearch> getFacetedSearchTable() {

        EntityTable<FacetedSearch> table = new EntityTable<FacetedSearch>(
                esiContainer);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

        table.setHeight("100%");
        table.setPageLength(20);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Object value = event.getProperty().getValue();
                EntityItem<FacetedSearch> obj = esiContainer.getItem(value);
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
//        table.addColumns(FacetedSearch_.name);
        return table;

    }

    protected void entitySelected(FacetedSearch facetedSearch) {
        if(searchListener != null) {
            searchListener.onSelect(facetedSearch);
        }
    }

    public void addSearchListener(
            SearchSelectEventListener searchSelectEventListener) {
        this.searchListener = searchSelectEventListener;
    }
}

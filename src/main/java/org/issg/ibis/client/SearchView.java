package org.issg.ibis.client;

import org.issg.ibis.domain.FacetedSearch;
import org.issg.ibis.domain.FacetedSearch_;
import org.issg.ibis.domain.ResourceType;
import org.jrc.form.editor.EditorPanelHeading;
import org.jrc.form.editor.EntityTable;
import org.jrc.form.filter.FilterPanel;
import org.jrc.persist.ContainerManager;
import org.jrc.persist.Dao;
import org.jrc.ui.SimpleHtmlHeader;
import org.jrc.ui.SimpleHtmlLabel;
import org.jrc.ui.SimplePanel;
import org.jrc.ui.baseview.TwinPanelView;

import com.google.inject.Inject;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class SearchView extends HorizontalLayout implements View {

    private JPAContainer<FacetedSearch> esiContainer;

    private VerticalLayout content = new VerticalLayout();

    private Dao dao;

    private LayerViewer layerViewer;

    GridLayout gl = new GridLayout(3, 3);

    @Inject
    public SearchView(Dao dao) {
        ContainerManager<FacetedSearch> containerManager = new ContainerManager<FacetedSearch>(
                dao, FacetedSearch.class);
        this.esiContainer = containerManager.getContainer();
        this.dao = dao;

        {
            setSizeFull();

            SimplePanel leftPanel = new SimplePanel();
            leftPanel.setSizeFull();


            addComponent(leftPanel);

//            leftPanel.addComponent(new EditorPanelHeading("Search"));

            content.setSizeFull();
            leftPanel.addComponent(content);
            setComponentAlignment(leftPanel, Alignment.MIDDLE_CENTER);


            HorizontalLayout hl = new HorizontalLayout();
            hl.setSizeFull();
            content.addComponent(hl);
            content.setExpandRatio(hl, 1);

            hl.addComponent(getFilterPanel());
            hl.addComponent(getFacetedSearchTable());

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

//            HorizontalLayout hl = new HorizontalLayout();
            
//            esi.get

//            hl.addComponent(new SimpleHtmlLabel("<div class='search-result-"+esi.getLabel()+"'>"+esi.getLabel()+"<div>"));
//            hl.addComponent(new SimpleHtmlLabel(getLink(esi)));

//            hl.addComponent(new SimpleHtmlLabel(esi.getStudy().getStudyName()));
            ResourceType rt = facetedSearch.getResourceType();
            
            String html = "<div class='search-result result-type-" + rt.getId() + "'>" + rt.getId() + "</div>";
            html += getLink(facetedSearch);
            
            html += ("<div>Known impacts: " + facetedSearch.getImpactCount() + "</div>");
            

            SimpleHtmlLabel shl = new SimpleHtmlLabel(html);
//            shl.setStyleName("search-result");

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

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

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
                if (value == null) {
                    return;
                }
                String si = (String) value;
                entitySelected(si);

            }
        });

        ImpactVisualizationColumn generatedColumn = new ImpactVisualizationColumn();
        table.addGeneratedColumn("id", generatedColumn);
        table.setColumnWidth("id", 400);
        return table;

    }

    protected void entitySelected(String si) {

        // if (id == null) {
        // //Todo - nothing is selected - clear the view, or not?
        // return;
        // }
        //
        //
        // EntityItem<FacetedSearch> x = esiContainer.getItem(id);
        // FacetedSearch entity = x.getEntity();
        //
        // if (entity == null) {
        // return;
        // }
        //
        // IndicatorSurface indicatorSurface = entity.getIndicatorSurface();
        // if (indicatorSurface != null) {
        // String layerName = indicatorSurface.getLayerName();
        // if (layerName != null) {
        // layerViewer.addWmsLayer(layerName);
        // }
        //
        // Polygon env = indicatorSurface.getEnvelope();
        // if (env != null) {
        // layerViewer.zoomTo(env);
        // }
        //
        // }

        // gl.addComponent(new Label(entity.getEcosystemService().toString()),
        // 0,0);
        // gl.addComponent(new Label(entity.getComments()), 0,1);
        // gl.addComponent(new Label(entity.getMinimumMappingUnit()), 0,2);
        // gl.addComponent(new Label(entity.getIndicator().toString()), 1,2);
    }
}

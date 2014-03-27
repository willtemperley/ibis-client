package org.issg.ibis.client.deprecated;

import org.issg.ibis.HeaderView;
import org.issg.ibis.MenuView;
import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.form.inject.GuicedViewProvider;
import org.vaadin.addons.guice.ui.ScopedUI;

import com.google.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

@Theme("dashboard")
public class IbisUI extends ScopedUI  {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private VerticalLayout rootLayout = new VerticalLayout();
    
	/*
	 * This holds the dynamically changing content
	 */
    private CssLayout content = new CssLayout();

	private Navigator nav;

	private GuicedViewProvider viewProvider;

    private HeaderView headerView;

    private MenuView menuView;

    private IbisMap ibisMap;
	
	
	@Inject
	public IbisUI(Dao dao, GuicedViewProvider viewProvider, HeaderView headerView, MenuView menuView, IbisMap ibisMap) {

		this.viewProvider = viewProvider;
		this.headerView = headerView;
		this.menuView = menuView;
		this.ibisMap = ibisMap;

	}

	@Override
	protected void init(VaadinRequest request) {
		
		/*
		 * Set up main panels
		 */
		setContent(rootLayout);
        rootLayout.addStyleName("dashboard-view");
		rootLayout.setSizeFull();
		
		headerView.setHeight("80px");
		rootLayout.addComponent(headerView);
		
		HorizontalLayout hl = new HorizontalLayout();
		
		rootLayout.addComponent(hl);
		hl.setSizeFull();
		rootLayout.setExpandRatio(hl, 1);
		
		hl.addComponent(content);
		hl.addComponent(ibisMap);
		ibisMap.setSizeFull();
		content.setSizeFull();

		nav = new Navigator(this, content);
		nav.addProvider(viewProvider);

	}
	

}

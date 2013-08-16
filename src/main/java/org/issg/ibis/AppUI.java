package org.issg.ibis;

import org.jrc.form.view.GuicedViewProvider;

import org.jrc.persist.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;

//@Theme("biopama")
@Theme("dashboard")
public class AppUI extends ScopedUI  {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private CssLayout rootLayout = new CssLayout();
    
	/*
	 * This holds the dynamically changing content
	 */
    private CssLayout content = new CssLayout();

	private Navigator nav;

	private GuicedViewProvider viewProvider;

    private HeaderView headerView;

    private MenuView menuView;
	
	
	@Inject
	public AppUI(Dao dao, GuicedViewProvider viewProvider, HeaderView headerView, MenuView menuView) {
		this.viewProvider = viewProvider;
		this.headerView = headerView;
		this.menuView = menuView;
		logger.debug("UI created.");
	}

	@Override
	protected void init(VaadinRequest request) {
		
		/*
		 * Set up main panels
		 */
		setContent(rootLayout);
        rootLayout.addStyleName("dashboard-view");
		rootLayout.setSizeFull();
		
//		rootLayout.addComponent(headerView);
//		rootLayout.addComponent(menuView);
		rootLayout.addComponent(content);
		
//		header.addComponent(headerView);
		
		nav = new Navigator(this, content);
		nav.addProvider(viewProvider);
	}
	

}

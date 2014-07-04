package org.issg.ibis.responsive;

import org.jrc.persist.Dao;
import org.vaadin.addons.form.inject.GuicedViewProvider;
import org.vaadin.addons.guice.ui.ScopedUI;

import com.google.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

@Theme("responsive")
//@Theme("touchkit")
public class IbisUI extends ScopedUI {

	Dao dao;
	MenuLayout root = new MenuLayout();
	private VerticalLayout rootLayout = new VerticalLayout();
	private HeaderView headerView;

	@Inject
	public IbisUI(Dao dao, GuicedViewProvider viewProvider, HeaderView headerView) {
		this.dao = dao;
		this.headerView = headerView;
		Navigator nav = new Navigator(this, root);
		nav.addProvider(viewProvider);
	}

	@Override
	protected void init(VaadinRequest request) {
		setContent(rootLayout);
		rootLayout.setSizeFull();
		rootLayout.addComponent(headerView);
		headerView.setHeight("40px");
		rootLayout.addComponent(root);
		root.setSizeFull();
		rootLayout.setExpandRatio(root, 1);
	}

	class MenuLayout extends CssLayout {
		public MenuLayout() {
			setSizeFull();
			addStyleName("menulayout");
			Responsive.makeResponsive(this);
		}

	}

}

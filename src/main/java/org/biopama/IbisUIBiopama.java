package org.biopama;

import com.vaadin.ui.*;

import org.biopama.edit.Dao;
import org.biopama.server.GuicedViewProvider;
import org.vaadin.addons.guice.ui.ScopedUI;

import com.google.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;

@Theme("responsive")
public class IbisUIBiopama extends ScopedUI {

	private Dao dao;
	private CssLayout contentRoot = new CssLayout();
	private VerticalLayout rootLayout = new VerticalLayout();
	private HeaderView headerView;

	@Inject
	public IbisUIBiopama(GuicedViewProvider viewProvider, HeaderView headerView) {
		this.headerView = headerView;
		Navigator nav = new Navigator(this, contentRoot);
		nav.addProvider(viewProvider);
	}

	@Override
	protected void init(VaadinRequest request) {
		setContent(rootLayout);
		rootLayout.setSizeFull();
		rootLayout.addComponent(headerView);
		headerView.setHeight("40px");
		
//		VerticalLayout bg = new VerticalLayout();
//		bg.setSizeFull();
//		rootLayout.addComponent(bg);
		rootLayout.addStyleName("background-root");
		
//		rootLayout.addComponent(bg);
//		rootLayout.setExpandRatio(bg, 1);

		Label topSpacer = new Label();
		topSpacer.setHeight("10px");
		Label bottomSpacer = new Label();
		bottomSpacer.setHeight("10px");
		rootLayout.addComponent(topSpacer);
		rootLayout.addComponent(contentRoot);
		rootLayout.addComponent(bottomSpacer);
//		bg.addComponent(topSpacer);
//		bg.addComponent(contentRoot);
//		bg.addComponent(bottomSpacer);
		contentRoot.setSizeFull();
		rootLayout.setExpandRatio(contentRoot, 1);

		/* The contentRoot has the content */
//		contentRoot.addStyleName("content");

	}

//	class MenuLayout extends CssLayout {
//		public MenuLayout() {
//			setSizeFull();
////			addStyleName("menulayout");
//			Responsive.makeResponsive(this);
//		}
//
//	}
}

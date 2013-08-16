package org.issg.ibis;

import java.util.Collection;
import java.util.Map;

import org.jrc.form.AdminStringUtil;

import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * An index with links to all views
 * 
 * @author Will Temperley
 *
 */
public class IndexPage extends VerticalLayout implements View {


	@Inject
	public IndexPage(Map<String, Provider<View>> adminComponents, Multimap<String, String> menuTreeMap) {

		for (String key : menuTreeMap.keySet()) {

			VerticalLayout panelLayout = new VerticalLayout();
			Panel mainPanel = new Panel(key, panelLayout);
			this.addComponent(mainPanel);

			Collection<String> x = menuTreeMap.get(key);

			for (String url : x) {

				String[] headTail = url.split("/");
				String tail = headTail[1];
				Link editorLink = new Link(
						AdminStringUtil.splitCamelCase(tail),
						new ExternalResource("#!" + url));

				panelLayout.addComponent(editorLink);
			}
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}

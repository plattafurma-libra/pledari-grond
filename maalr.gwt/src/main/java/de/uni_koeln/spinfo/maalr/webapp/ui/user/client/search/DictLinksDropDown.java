package de.uni_koeln.spinfo.maalr.webapp.ui.user.client.search;

import java.util.List;

import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavPills;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class DictLinksDropDown extends NavPills {
	
	private String TARGET = "_blank";
	
	private Dictionary linksDictionary;
	private Dictionary localeDictionary;
	
	private Element extLinkContainer = DOM.getElementById("ext_links_container");
	private Element dropDownLinkContainer = getElement();
	
	public DictLinksDropDown() {
		this.linksDictionary = DictionaryConstants.getLinksDictionary();
		this.localeDictionary = DictionaryConstants.getLocaleDictionary();
		this.add(createDropdown());
		addResizeHandler();
		this.getElement().getStyle().setMarginTop(70, Unit.PX);
		this.getElement().getStyle().setMarginLeft(5, Unit.PX);
	}

	private Dropdown createDropdown() {
		
		Dropdown dropdown = new Dropdown(localeDictionary.get("select"));
		// dropdown.add(new NavHeader(localeDictionary.get("dict_label_lia")));
		
		dropdown.add(new NavLink(localeDictionary.get("puter"), linksDictionary.get("puter"), TARGET));
		dropdown.add(new NavLink(localeDictionary.get("surmiran"), linksDictionary.get("surmiran")));
		dropdown.add(new NavLink(localeDictionary.get("sursilvan"), linksDictionary.get("sursilvan"), TARGET));
		dropdown.add(new NavLink(localeDictionary.get("sutsilvan"), linksDictionary.get("sutsilvan")));
		dropdown.add(new NavLink(localeDictionary.get("vallader"), linksDictionary.get("vallader"), TARGET));
		
		//dropdown.add(new NavLink(localeDictionary.get("dict_ulteriurs"),  linksDictionary.get("pledari"), TARGET));
		
		dropdown.add(getLink(DictionaryConstants.DICT_LINKS_EXTERNAL));
		
		return dropdown;
		
	}
	
	private Widget getLink(final List<String> links) {
		
		NavLink widget = new NavLink(localeDictionary.get(links.get(0)));
		
		widget.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				new ExternalLinkDialog(links, DictionaryConstants.getLinksDictionary());

				event.getNativeEvent().preventDefault();
				event.getNativeEvent().stopPropagation();
			}
		});
		return widget;
	}

	private void addResizeHandler() {
		
		if(Window.getClientWidth() > 768) {
			extLinkContainer.getStyle().setVisibility(Visibility.VISIBLE);
			dropDownLinkContainer.getStyle().setVisibility(Visibility.HIDDEN);
		}
		
		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				
				if(dropDownLinkContainer != null) {
					if(event.getWidth() < 768) {
						dropDownLinkContainer.getStyle().setVisibility(Visibility.VISIBLE);
					} else {
						dropDownLinkContainer.getStyle().setVisibility(Visibility.HIDDEN);
					}
				}
					
				if(extLinkContainer != null) {
					if(event.getWidth() < 1028) {
						extLinkContainer.getStyle().setVisibility(Visibility.HIDDEN);
					} else {
						extLinkContainer.getStyle().setVisibility(Visibility.VISIBLE);
					}
				}
			}
			
		});
	}

}

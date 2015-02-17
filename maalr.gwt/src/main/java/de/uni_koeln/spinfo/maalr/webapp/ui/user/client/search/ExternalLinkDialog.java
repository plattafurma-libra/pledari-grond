/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client.search;

import java.util.List;

import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.base.ListItem;
import com.github.gwtbootstrap.client.ui.base.UnorderedList;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

/**
 *	A Modal/PopupDialog containing several external links.	
 * 
 *	@author Mihail Atanassov <atanassov.mihail@gmail.com>
 *
 */

public class ExternalLinkDialog extends Widget {

	private Modal modal;

	private Dictionary linksDictionary;

	private Dictionary localeDictionary;

	private List<String> links;

	public ExternalLinkDialog(List<String> links) {
		this.linksDictionary = DictionaryConstants.getLinksDictionary();
		this.localeDictionary = DictionaryConstants.getLocaleDictionary();
		this.links = links;
		init();
	}

	private void init() {
		createModalWithLinks();
		modal.show();
	}

	private void createModalWithLinks() {
		modal = new Modal(true);
		modal.setBackdrop(BackdropType.NORMAL);
		modal.add(new Heading(3, localeDictionary.get(links.get(0))));

		UnorderedList list = new UnorderedList();
		String ulClassName = list.getElement().getClassName();
		list.getElement().setClassName(ulClassName + " ext_links_dicts");

		for (int i = 1; i < links.size(); i++) {
			list.add(new ListItem(createLink(localeDictionary.get(links.get(i)),
					linksDictionary.get(links.get(i)))));
		}

		modal.add(list);
		
		String modalClassName = modal.getElement().getClassName();
		modal.getElement().setClassName(modalClassName + " vertical-center");
	}

	private Anchor createLink(final String txt, final String url) {
		Anchor anchor = new Anchor(txt, url);
		anchor.setTarget("_blank");
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				modal.hide();
				modal.removeFromParent();
			}
		});
		return anchor;
	}

}

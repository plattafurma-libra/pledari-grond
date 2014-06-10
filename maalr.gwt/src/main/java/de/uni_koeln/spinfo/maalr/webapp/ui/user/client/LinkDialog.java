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
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client;

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

public class LinkDialog extends Widget {

	private Modal modal;

	private Dictionary linksDictionary;

	private Dictionary localeDictionary;

	public LinkDialog() {
		this.linksDictionary = DictionaryConstants.getLinksDictionary();
		this.localeDictionary = DictionaryConstants.getLocaleDictionary();
		init();
	}

	private void init() {
		createModalWithLinks();
		modal.show();
	}

	private void createModalWithLinks() {
		modal = new Modal(true);
		modal.setBackdrop(BackdropType.NORMAL);
		modal.add(new Heading(3, localeDictionary.get(DictionaryConstants.DICT_LINK_LABEL)));

		UnorderedList list = new UnorderedList();
		String className = list.getElement().getClassName();
		list.getElement().setClassName(className + " ext_links_dicts");

		list.add(new ListItem(createLink(
				localeDictionary.get(DictionaryConstants.SURSILVAN),
				linksDictionary.get(DictionaryConstants.LINK_SURSILVAN))));
		list.add(new ListItem(createLink(
				localeDictionary.get(DictionaryConstants.PUTER),
				linksDictionary.get(DictionaryConstants.LINK_PUTER))));
		list.add(new ListItem(createLink(
				localeDictionary.get(DictionaryConstants.VALLADER),
				linksDictionary.get(DictionaryConstants.LINK_VALLADER))));
		list.add(new ListItem(createLink(
				localeDictionary.get(DictionaryConstants.PLEDARI),
				linksDictionary.get(DictionaryConstants.LINK_PLEDARI))));

		modal.add(list);
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

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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.order;

import java.util.ArrayList;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;

public class OrderWidget extends Composite {
	
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	interface MyUiBinder extends UiBinder<Widget, OrderWidget> {
	}
	
	private ListDataProvider<LemmaVersion> provider;
	
	@UiField
	CellTable<LemmaVersion> list;
	
	@UiField
	Button up;
	
	@UiField
	Button down;
	
//	@UiField
//	PageHeader header;

	private SingleSelectionModel<LemmaVersion> selectionModel;
	
	public OrderWidget() {
		initialize();
	}
	
	private void initialize() {
		selectionModel = new SingleSelectionModel<LemmaVersion>(new ProvidesKey<LemmaVersion>() {

			@Override
			public Object getKey(LemmaVersion item) {
				return item;
			}
		});
		provider = new ListDataProvider<LemmaVersion>();
		initWidget(uiBinder.createAndBindUi(OrderWidget.this));
//		header.setText("Modify Order");
		list.setPageSize(100);
		list.setSelectionModel(selectionModel);
		list.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		Column<LemmaVersion, SafeHtml> column = new Column<LemmaVersion, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(LemmaVersion object) {
				
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				LemmaDescription description = AsyncLemmaDescriptionLoader
						.getDescription();
				String toDisplay = description.toString(object, UseCase.RESULT_LIST, true)
						+ " ⇔ "
						+ description.toString(object, UseCase.RESULT_LIST, false);
				sb.appendHtmlConstant(toDisplay);
				return sb.toSafeHtml();
			}
		};
		RowStyles<LemmaVersion> wrapper = new RowStyles<LemmaVersion>() {

			@Override
			public String getStyleNames(LemmaVersion row, int rowIndex) {
				return "user-row";
			}
		};
		list.setRowStyles(wrapper);
		list.addColumn(column);
		provider.addDataDisplay(list);
		up.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				LemmaVersion obj = selectionModel.getSelectedObject();
				if(obj == null) return;
				int index = provider.getList().indexOf(obj);
				if(index == -1) return;
				if(index == 0) return;
				LemmaVersion other = provider.getList().get(index-1);
				provider.getList().set(index-1, obj);
				provider.getList().set(index, other);
				provider.refresh();
				
			}
		});
		down.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				LemmaVersion obj = selectionModel.getSelectedObject();
				if(obj == null) return;
				int index = provider.getList().indexOf(obj);
				if(index == -1) return;
				if(index == provider.getList().size()-1) return;
				LemmaVersion other = provider.getList().get(index+1);
				provider.getList().set(index, other);
				provider.getList().set(index+1, obj);
				provider.refresh();
			}
		});
	}
	
	public void setOrderedItems(ArrayList<LemmaVersion> items) {
		provider.getList().clear();
		provider.getList().addAll(items);
		provider.refresh();
		list.setRowCount(items.size());
		list.redraw();
		selectionModel.setSelected(items.get(0), true);
	}

	public ArrayList<LemmaVersion> getOrderedItems() {
		return new ArrayList<LemmaVersion>(provider.getList());
	}

//	public void setSubtext(String string) {
//		header.setSubtext(string);
//	}
	
}

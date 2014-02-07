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
package de.uni_koeln.spinfo.maalr.webapp.ui.common.client;

import com.github.gwtbootstrap.client.ui.DataGrid;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.HasRows;

import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.LazyLoadEvent;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.LazyLoadHandler;

public class PagingDataGrid<T> extends DataGrid<T>{

	public ScrollPanel getScrollPanel() {
        HeaderPanel header = (HeaderPanel) getWidget();
        return (ScrollPanel) header.getContentWidget();
    }
	
	private int incrementSize = 20;
	private HandlerManager handlerManager = new HandlerManager(this);
	
	public int getIncrementSize() {
		return incrementSize;
	}

	public void setIncrementSize(int incrementSize) {
		this.incrementSize = incrementSize;
	}
	

	public interface TableRes extends DataGrid.Resources {
		@Source({DataGrid.Style.DEFAULT_CSS, "de/uni_koeln/spinfo/maalr/webapp/ui/admin/client/user/list/UserList.css"})
		Style dataGridStyle();

		interface Style extends DataGrid.Style {}
		}

	public PagingDataGrid() {
		this(20);
	}
	
	public PagingDataGrid(int pageSize) {
		super(pageSize-1, (Resources) GWT.create(TableRes.class));
		this.incrementSize = pageSize;
		getScrollPanel().addScrollHandler(new ScrollHandler() {
			
			private int lastScrollPos = 0;

			@Override
			public void onScroll(ScrollEvent event) {
				// If scrolling up, ignore the event.
				getScrollPanel().getElement().focus();
				int oldScrollPos = lastScrollPos ;
				ScrollPanel scrollable = getScrollPanel();
				lastScrollPos = scrollable.getVerticalScrollPosition();
				if (oldScrollPos >= lastScrollPos) {
					return;
				}

				HasRows display = PagingDataGrid.this;
				int maxScrollTop = scrollable.getWidget().getOffsetHeight()
						- scrollable.getOffsetHeight();
				if (lastScrollPos >= maxScrollTop) {
					handlerManager.fireEvent(new LazyLoadEvent(PagingDataGrid.this));
//					// We are near the end, so increase the page size.
//					int newPageSize = Math.min(display.getVisibleRange()
//							.getLength() + getIncrementSize(), display.getRowCount());
//					display.setVisibleRange(0, newPageSize);
				}
			}

		});
		
	}
	
	public void addLoadHandler(LazyLoadHandler handler) {
		handlerManager.addHandler(LazyLoadEvent.TYPE, handler);
	}

}

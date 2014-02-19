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
package de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events;

import com.google.gwt.event.shared.GwtEvent;

import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.PagingDataGrid;

public class LazyLoadEvent extends GwtEvent<LazyLoadHandler>{
	
	public static final Type<LazyLoadHandler> TYPE = new Type<LazyLoadHandler>();
	private PagingDataGrid<?> dataGrid;
	
	public PagingDataGrid<?> getDataGrid() {
		return dataGrid;
	}

	public LazyLoadEvent(PagingDataGrid<?> pagingDataGrid) {
		this.dataGrid = pagingDataGrid;
	}
	
	@Override
	public Type<LazyLoadHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LazyLoadHandler handler) {
		handler.onLoad(this);
	}
	
}

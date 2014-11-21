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

import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;

public class SearchEvent extends GwtEvent<SearchHandler>{
	
	public static final Type<SearchHandler> TYPE = new Type<SearchHandler>();
	
	private MaalrQuery query;
	
	public MaalrQuery getQuery() {
		return query;
	}

	public SearchEvent(MaalrQuery query) {
		this.query = query;
	}
	
	@Override
	public Type<SearchHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SearchHandler handler) {
		handler.onSearch(this);
	}
	
}

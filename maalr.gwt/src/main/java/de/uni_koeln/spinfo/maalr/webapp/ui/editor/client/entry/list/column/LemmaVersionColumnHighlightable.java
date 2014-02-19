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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.column;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Highlighter;

public class LemmaVersionColumnHighlightable extends
		Column<LemmaVersion, SafeHtml> {

	private MaalrQuery query;

	public LemmaVersionColumnHighlightable(SafeHtmlCell cell) {
		super(cell);
	}

	@Override
	public SafeHtml getValue(LemmaVersion object) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		LemmaDescription description = AsyncLemmaDescriptionLoader
				.getDescription();
		String toDisplay = description.toString(object, UseCase.RESULT_LIST, true)
				+ " ⇔ "
				+ description.toString(object, UseCase.RESULT_LIST, false);
		if(query != null && query.isHighlight()) {
			toDisplay = Highlighter.highlight(toDisplay, query.getValue("searchPhrase"));
		}
		sb.appendHtmlConstant(toDisplay);
		return sb.toSafeHtml();
	}

	public void setQuery(MaalrQuery query) {
		this.query = query;
	}
}

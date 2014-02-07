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
package de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryModifierOption;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrField;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneHelper;

public abstract class AbstractQueryBuilder implements MaalrQueryBuilder {

	protected String srcFieldName, destFieldName;
	
	protected Analyzer analyzer = LuceneHelper.newAnalyzer();
	protected String destValue;
	private String reference;
	

	public String getSrcFieldName() {
		return srcFieldName;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}

	public void setOption(QueryModifierOption option) {
		srcFieldName = option.getField().getFrom();
		destFieldName = option.getField().getModify();
		destValue = option.getValue().getModify();
	}

	public abstract List<Query> transform(MaalrField field);
	
	public String getReference() {
		return reference;
	}

}

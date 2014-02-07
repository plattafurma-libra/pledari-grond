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

import java.util.Arrays;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrField;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneHelper;
import de.uni_koeln.spinfo.maalr.lucene.util.TokenizerHelper;

/**
 * A query builder to generate 'exact' queries. Matches both upper and lowercase.
 * See {@link MaalrQueryBuilder} and {@link DefaultQueryBuilder} for details.
 * @author sschwieb
 *
 */
public class ExactMatchQueryBuilder extends AbstractQueryBuilder {

	public List<Query> transform(MaalrField field) {
		String fieldName = destFieldName.replace("${field}", field.getField());
		String value = destValue.replace("${phrase}", field.getValue());
		value = TokenizerHelper.tokenizeString(LuceneHelper.newWhitespaceAnalyzer(), value);
		// match both upper and lower case:
		Query query = new TermQuery(new Term(fieldName + "_exact", value.toLowerCase())); 
		return Arrays.asList(query);
	}
	
}

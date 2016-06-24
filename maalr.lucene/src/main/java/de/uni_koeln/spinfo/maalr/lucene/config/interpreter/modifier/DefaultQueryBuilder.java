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
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.MaalrFieldType;
import de.uni_koeln.spinfo.maalr.lucene.config.LuceneIndexManager;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.util.TokenizerHelper;

/**
 * This query builder generates three lucene-queries from
 * a single {@link MaalrField}, to modify the sort order of
 * the results of a query: If a user searches for a term (for instance, 
 * 'car'), all exact translations should be listed before any
 * translation which contains the term among others ('car insurance', 
 * 'car crash', etc). This is internally realized by using both
 * {@link TermQuery} and {@link PrefixQuery}, and by modifying the
 * field names which are searched by lucene.
 * <br>
 * 
 * @author sschwieb
 *
 */
public class DefaultQueryBuilder extends MaalrQueryBuilder {
	
	private static Logger logger = LoggerFactory.getLogger(LuceneIndexManager.class);


	@Override
	protected void buildColumnToFieldsMapping() {
		/*
		 * Add field variations with additional features to index.
		 */
		registerFieldMapping("first", false, MaalrFieldType.STRING, true, false);
		registerFieldMapping("second",true, MaalrFieldType.TEXT, true, false);
	}

	@Override
	public List<Query> transform(String value) {
		value = TokenizerHelper.tokenizeString(analyzer, value);
		//match single word entries 
		TermQuery first = new TermQuery(new Term(super.getFieldName("first"), value));
		first.setBoost(1000f);
		//match entries where searchphrase is followed by whitespace
		TermQuery second = new TermQuery(new Term(super.getFieldName("second"), value));
		second.setBoost(100f);
		//also match prefix of StringFields ...
		PrefixQuery third = new PrefixQuery(new Term(super.getFieldName("first"), value));
		third.setBoost(10f);
		//and of (analyzed) TextFields
		PrefixQuery fourth = new PrefixQuery(new Term(super.getFieldName("second"), value));
		
		/* FIXME workaround to handle bracketed expressions by using hard-coded fields containing orthographic variants. */
		String fieldName = super.getFieldName("second");
		if(fieldName.startsWith("DTags") || fieldName.startsWith("RTags")){
			Query brackets = new PrefixQuery(new Term(super.getFieldName("second"), value));
			brackets.setBoost(10f);
//			logger.info("bracket query: "+brackets);
			return Arrays.asList(brackets);
		}
		//else:
		return Arrays.asList(first,second,third,fourth);
		
	}
	
	

}

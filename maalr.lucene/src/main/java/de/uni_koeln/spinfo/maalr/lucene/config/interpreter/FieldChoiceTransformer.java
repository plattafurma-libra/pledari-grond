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
package de.uni_koeln.spinfo.maalr.lucene.config.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.FieldChoiceField;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.FieldChoiceOption;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;

/**
 * A {@link FieldChoiceTransformer} is responsible to map the user-selected item
 * of a fieldchoice-element in the search configuration onto one or more 
 * {@link MaalrField} objects, which finally are used to create a lucene query.
 * <br>
 * To give an example: Imagine the search interface should contain a "language" combo box,
 * where it is possible to select the values "German", "English" and "Both".
 * This would involve two different fields to search in (with lucene), for instance
 * "german_phrase" and "english_phrase". "German" and "English" would be configured
 * to return one field, whereas "Both" would return both fields.
 * 
 * @author sschwieb
 *
 */
public class FieldChoiceTransformer {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<FieldChoiceField> fields;
	private String valueOf;

	public String getValueOf() {
		return valueOf;
	}

	public FieldChoiceTransformer(String valueOf, FieldChoiceOption option) {
		this.valueOf = valueOf;
		this.fields = option.getFields();
	}

	/**
	 * Partially transforms a {@link MaalrQuery} into a list of {@link MaalrField}-objects
	 * which can be logically "or'ed". 
	 * @param maalrQuery
	 * @return
	 */
	public List<MaalrField> transform(MaalrQuery maalrQuery) {
		if(fields == null) return null;
		if(logger.isDebugEnabled()) {
			logger.debug("Transformer for " + fields + " and " + valueOf + ": " + maalrQuery);
		}
		Map<String, String> map = maalrQuery.getQueryMap();
		String string = map.get(valueOf);
		if(string == null || string.trim().length() == 0) return null;
		List<MaalrField> toReturn = new ArrayList<MaalrField>();
		for (FieldChoiceField field : fields) {
			toReturn.add(new MaalrField(field.getReference(), string));
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Transformer result: " + toReturn);
		}
		return toReturn;
	}

}

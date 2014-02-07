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
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.IndexedItem;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.MaalrFieldType;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneHelper;
import de.uni_koeln.spinfo.maalr.lucene.util.TokenizerHelper;

/**
 * A {@link FieldFactory} is responsible for converting an {@link IndexedItem}
 * into one or more {@link IndexableField} objects, thus creating a lucene index
 * from a maalr search configuration. Depending on the {@link MaalrFieldType},
 * it generates either an {@link StringField}, {@link TextField}, or {@link IntField}
 * object. In case of {@link MaalrFieldType#CSV}, a list of {@link StringField}
 * is generated.
 * 
 * @author sschwieb
 *
 */
public class FieldFactory {

	private final MaalrFieldType type;
	private final String name;
	private final Store stored;
	private final boolean lowercase;
	private final boolean analyzed;
	private final Analyzer analyzer;

	public FieldFactory(IndexedItem item) {
		type = item.getType();
		name = item.getDest();
		lowercase = item.isLowerCase();
		analyzed = item.isAnalyzed();
		stored = item.isStored() ? Store.YES : Store.NO;
		//workaround to enable special chars in search oracles (editor backend)
		analyzer = (item.isWhitespaceAnalyzer()) ? LuceneHelper.newWhitespaceAnalyzer() : LuceneHelper.newAnalyzer();
	}

	@SuppressWarnings("unchecked")
	public List<IndexableField> getFields(String value) {
		if(value == null) return null;
		if(lowercase) {
			value = value.toLowerCase();
		}
		if(analyzed) {
			value = TokenizerHelper.tokenizeString(analyzer, value);
		}
		switch(type) {
			case STRING: return (List) Arrays.asList(new StringField(name, value, stored));
			case TEXT: return (List) Arrays.asList(new TextField(name, value, stored));
			case INTEGER: return (List) Arrays.asList(new IntField(name, Integer.parseInt(value), stored));
			case CSV: return csvFields(name, value, stored);
		}
		throw new RuntimeException("Unsupported field type: " + value);
	}

	private List<IndexableField> csvFields(String name, String value, Store stored) {
		List<IndexableField> fields = new ArrayList<IndexableField>();
		String[] splitted = value.split(",");
		for (String part : splitted) {
			if(part.trim().length() > 0) {
				fields.add(new StringField(name, part.trim(), stored));
			}
		}
		return fields;
	}


}

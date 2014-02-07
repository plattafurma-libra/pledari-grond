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

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryModifierOption;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrField;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrQueryBuilder;

/**
 * This {@link MaalrQueryBuilder} can be used if a query modification
 * can be defined purely configurational. This is the case when only
 * the name of the field to search in and the search term itself have to
 * be modified. The strings ${field} and ${phrase} will be replaced with
 * the values passed into {@link SimpleModifierTransformer#transform(MaalrField)},
 * generating - given the example below and the field/value pair 'english' and
 * 'car' - field and value strings like 'english_some_modifier'
 * and 'car~0.6'.
 * <br><br>
 * <code>
 *  &lt;queryModifier id="soft"><br>
    &lt;field from="language"><br>
        &lt;modification>${field}_some_modifier&lt;/modification><br>
    &lt;/field><br>
    &lt;value from="phrase"><br>
        &lt;modification>${phrase}~0.6&lt;/modification><br>
    &lt;/value><br>
	&lt;/queryModifier><br>
</code>
<br>
 * See {@link DefaultQueryBuilder} for an alternative way to create query
 * modifications, which is more powerful but requires either customized subclasses
 * of {@link MaalrQueryBuilder} or proprietary naming conventions on field names.
 * @author sschwieb
 *
 */
public class SimpleModifierTransformer implements MaalrQueryBuilder {
	
	private String srcFieldName, destFieldName;

	private String destValue;
	private String reference;
	
	public String getSrcFieldName() {
		return srcFieldName;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}

	@Override
	public void setOption(QueryModifierOption option) {
		srcFieldName = option.getField().getFrom();
		destFieldName = option.getField().getModify();
		destValue = option.getValue().getModify();
	}
	
	public List<Query> transform(MaalrField field) {
		Query tq = new TermQuery(new Term(destFieldName.replace("${field}", field.getField()), destValue.replace("${phrase}", field.getValue())));
		return Arrays.asList(tq);
	}

	public String getReference() {
		return reference;
	}

	
	
	

}

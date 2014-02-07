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

import java.util.List;

import org.apache.lucene.search.Query;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryModifierOption;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.DefaultQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.SimpleModifierTransformer;

/**
 * A query builder is responsible for converting a single {@link MaalrField}
 * into one or more {@link Query} parts. Currently, the conversion can be
 * performed on two different ways: Either programmatically, by creating
 * a class which implements this interface, or by defining the transformation
 * through xml only. The latter one only allows a limited conversion, whereas
 * the programmatically modification offers direct access to all lucene-features.
 * <br>
 * See {@link SimpleModifierTransformer} and {@link DefaultQueryBuilder} for
 * details and examples. 
 * 
 * @author sschwieb
 *
 */
public interface MaalrQueryBuilder {
	
	public String getSrcFieldName();

	public void setReference(String reference);
	
	public List<Query> transform(MaalrField field);

	public String getReference();

	public void setOption(QueryModifierOption option);
	
	
	

}

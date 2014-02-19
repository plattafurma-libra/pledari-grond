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
package de.uni_koeln.spinfo.maalr.antlr.client;

import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import com.google.gwt.core.client.EntryPoint;

import de.uni_koeln.spinfo.maalr.antlr.shared.generated.MaalrLexer;
import de.uni_koeln.spinfo.maalr.antlr.shared.generated.MaalrParser;

public class Antlr4Maalr implements EntryPoint {
	
	@Override
	public void onModuleLoad() {
	}
		
	public Map<String,String> validate(String data) throws RecognitionException {
		ANTLRStringStream in = new ANTLRStringStream(data);
		MaalrLexer lexer = new MaalrLexer(in);
		lexer.reset();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MaalrParser parser = new MaalrParser(tokens);
        Map<String,String> result = parser.entry();
        return result;
		
	}

}

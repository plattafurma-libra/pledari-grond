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
package de.uni_koeln.spinfo.maalr.antlr;

import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Assert;
import org.junit.Test;

import de.uni_koeln.spinfo.maalr.antlr.shared.generated.MaalrLexer;
import de.uni_koeln.spinfo.maalr.antlr.shared.generated.MaalrParser;

public class ParserTests {
	
    public static final String R_GRAMMATIK = "RGrammatik";
    public static final String D_GRAMMATIK = "DGrammatik";
    public static final String R_SUBSEMANTIK = "RSubsemantik";  
    public static final String D_SUBSEMANTIK = "DSubsemantik";
    public static final String R_SEMANTIK = "RSemantik";  
    public static final String D_SEMANTIK = "DSemantik";
    public static final String R_GENUS = "RGenus";
    public static final String D_GENUS = "DGenus";  
    public static final String R_STICHWORT = "RStichwort";
    public static final String D_STICHWORT = "DStichwort";
	

	private Map<String,String> parse(String input) throws RecognitionException {
		ANTLRStringStream in = new ANTLRStringStream(input);
		MaalrLexer lexer = new MaalrLexer(in);
		lexer.reset();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MaalrParser parser = new MaalrParser(tokens);
        Map<String,String> result = parser.entry();
        return result;
	}
	
	@Test
	public void testSimpleTranslation() throws RecognitionException {
		Map<String,String> result = parse("hund <> dog");
		Assert.assertNotNull(result);
		Assert.assertTrue(result.size() > 0);
		validateLemma("hund", result);
		validateTranslation("dog", result);
	}
	
	@Test
	public void testComplexTranslation() throws RecognitionException {
		Map<String,String> result = parse("hund [m, refl] {Köter} {{Töle}} <> dog [m(f), adj]");
		Assert.assertNotNull(result);
		Assert.assertTrue(result.size() > 0);
		validateLemma("hund", result);
		validateTranslation("dog", result);
		validateLemmaGramm("refl", result);
		validateLemmaGenus("m", result);
		validateLemmaSem("Köter", result);
		validateLemmaSubsem("Töle", result);
		validateTranslatGramm("adj", result);	
		validateTranslatGenus("m(f)", result);
	}
	
//	@Ignore
	@Test(expected=RecognitionException.class)
	public void testFailedTranslation() throws RecognitionException {
		Map<String,String> result = parse("");
		Assert.assertNotNull(result);
		Assert.assertTrue(result.size() > 0);
	}

	private void validateLemma(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(D_STICHWORT));
		Assert.assertEquals(expected, result.get(D_STICHWORT));
	}
	
	private void validateTranslation(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(R_STICHWORT));
		Assert.assertEquals(expected, result.get(R_STICHWORT));
	}
	
	private void validateLemmaGramm(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(D_GRAMMATIK));
		Assert.assertEquals(expected, result.get(D_GRAMMATIK));
	}
	
	private void validateLemmaGenus(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(D_GENUS));
		Assert.assertEquals(expected, result.get(D_GENUS));
	}
	
	private void validateLemmaSem(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(D_SEMANTIK));
		Assert.assertEquals(expected, result.get(D_SEMANTIK));
	}
	private void validateLemmaSubsem(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(D_SUBSEMANTIK));
		Assert.assertEquals(expected, result.get(D_SUBSEMANTIK));
	}
	private void validateTranslatGramm(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(R_GRAMMATIK));
		Assert.assertEquals(expected, result.get(R_GRAMMATIK));
	}
	
	private void validateTranslatGenus(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(R_GENUS));
		Assert.assertEquals(expected, result.get(R_GENUS));
	}
	
	private void validateTranslatSem(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(R_SEMANTIK));
		Assert.assertEquals(expected, result.get(R_SEMANTIK));
	}
	
	private void validateTranslatSubsem(String expected, Map<String,String> result) {
		Assert.assertNotNull(result.get(R_SUBSEMANTIK));
		Assert.assertEquals(expected, result.get(R_SUBSEMANTIK));
	}

}

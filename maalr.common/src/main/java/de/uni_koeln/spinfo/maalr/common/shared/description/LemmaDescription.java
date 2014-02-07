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
package de.uni_koeln.spinfo.maalr.common.shared.description;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.StructuredEntry;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LemmaDescription implements Serializable {

	private static final long serialVersionUID = 8271576000014422713L;
	
	@XmlElementWrapper(name="values")
	@XmlElement(name="value")
	private ArrayList<ValueSpecification> values = new ArrayList<ValueSpecification>();
	
	@XmlElementWrapper(name="result_list_lang_a")
	@XmlElement(name="valueFormat")
	private ArrayList<ValueFormat> resultListLangA = new ArrayList<ValueFormat>();
	
	@XmlElementWrapper(name="result_list_lang_b")
	@XmlElement(name="valueFormat")
	private ArrayList<ValueFormat> resultListLangB = new ArrayList<ValueFormat>();
	
	@XmlElementWrapper(name="editor_list_lang_a")
	@XmlElement(name="item")
	private ArrayList<String> editorLangA = new ArrayList<String>();
	
	@XmlElementWrapper(name="editor_list_lang_b")
	@XmlElement(name="item")
	private ArrayList<String> editorLangB = new ArrayList<String>();
	
	@XmlElementWrapper(name="user_suggest_list_lang_a")
	@XmlElement(name="item")
	private ArrayList<String> userSuggestLangA = new ArrayList<String>();
	
	@XmlElementWrapper(name="user_suggest_list_lang_b")
	@XmlElement(name="item")
	private ArrayList<String> userSuggestLangB = new ArrayList<String>();
	
	
	@XmlElementWrapper(name="dict_list_lang_a")
	@XmlElement(name="item")
	private ArrayList<String> dictLangA = new ArrayList<String>();
	
	@XmlElementWrapper(name="dict_list_lang_b")
	@XmlElement(name="item")
	private ArrayList<String> dictLangB = new ArrayList<String>();
	
	@XmlElement(name="language_a")
	private String languageA;
	
	@XmlElement(name="language_b")
	private String languageB;
	
	@XmlElement(name="dict_index_lang_a")
	private String dictFieldLangA;
	
	@XmlElement(name="dict_index_lang_b")
	private String dictFieldLangB;
	
	@XmlElementWrapper(name="sort_list_lang_a")
	@XmlElement(name="item")
	private ArrayList<String> sortListLangA = new ArrayList<String>();
	
	@XmlElementWrapper(name="sort_list_lang_b")
	@XmlElement(name="item")
	private ArrayList<String> sortListLangB = new ArrayList<String>();
	
	@XmlElement(name="sort_order_lang_a")
	private String sortOrderLangA;

	@XmlElement(name="sort_order_lang_b")
	private String sortOrderLangB;
	
//	public String getLanguageA() {
//		return languageA;
//	}
//
	public void setLanguageA(String languageA) {
		this.languageA = languageA;
	}
//
//	public String getLanguageB() {
//		return languageB;
//	}
//
	public void setLanguageB(String languageB) {
		this.languageB = languageB;
	}

	public ArrayList<String> getDictListLangA() {
		return dictLangA;
	}
	public ArrayList<String> getDictListLangB() {
		return dictLangB;
	}
	public void setResultListLangA(ArrayList<ValueFormat> resultListLangA) {
		this.resultListLangA = resultListLangA;
	}

	public void setResultListLangB(ArrayList<ValueFormat> resultListLangB) {
		this.resultListLangB = resultListLangB;
	}

	public void setEditorLangA(ArrayList<String> editorLangA) {
		this.editorLangA = editorLangA;
	}

	public void setEditorLangB(ArrayList<String> editorLangB) {
		this.editorLangB = editorLangB;
	}

	public ArrayList<ValueFormat> getResultListLangA() {
		return resultListLangA;
	}

	public ArrayList<ValueFormat> getResultListLangB() {
		return resultListLangB;
	}

	public ArrayList<String> getEditorLangA() {
		return editorLangA;
	}

	public ArrayList<String> getEditorLangB() {
		return editorLangB;
	}

	//private HashMap<String, ValueSpecification> byId;

	public ArrayList<ValueSpecification> getValues() {
		return values;
	}

	public void setValues(ArrayList<ValueSpecification> values) {
		this.values = values;
	}

	public void addField(ValueSpecification valueSpecification) {
		values.add(valueSpecification);
	}


	public String getLanguageName(boolean firstLanguage) {
		if(firstLanguage) return languageA;
		return languageB;
	}
	
	public String toString(LemmaVersion lemma, UseCase useCase, boolean firstLanguage) {
		return toString(lemma, useCase, firstLanguage, null);
	}
	
	public String toString(LemmaVersion lemma, UseCase useCase, boolean firstLanguage, Escaper escaper) {
		switch(useCase) {
			case RESULT_LIST : {
				List<ValueFormat> keys = firstLanguage ? getResultListLangA() : getResultListLangB();
				return format(lemma, keys, escaper);
			}
			case ALPHA_INDEX: {
				List<ValueFormat> keys = firstLanguage ? getResultListLangA() : getResultListLangB();
				return format(lemma, keys.subList(0, 1), escaper);
			}
		default: return "--";
		}
	}

	private String format(LemmaVersion lemma, List<ValueFormat> keys) {
		return format(lemma, keys, null);
	}
	
	private String format(LemmaVersion lemma, List<ValueFormat> keys, Escaper escaper) {
		StringBuilder builder = new StringBuilder();
		List<ValueFormat> used = new ArrayList<ValueFormat>();
		for (ValueFormat key : keys) {
			String value = lemma.getEntryValue(key.getKey());
			if(value != null) {
				used.add(key);
			}
		}
		for(int i = 0; i < used.size(); i++) {
			builder.append(used.get(i).apply(lemma, escaper));
			if(i < used.size()-1) {
				builder.append(" ");
			}
		}
		return builder.toString().trim();
	}
	public ArrayList<String> getFields(UseCase useCase, boolean firstLanguage) {
		if(useCase == UseCase.FIELDS_FOR_SIMPLE_EDITOR) {
			if(firstLanguage) {
				return userSuggestLangA;
			} else {
				return userSuggestLangB;
			}
		}
		if(useCase == UseCase.FIELDS_FOR_ADVANCED_EDITOR) {
			if(firstLanguage) {
				return editorLangA;
			} else {
				return editorLangB;
			}
		}
		if(useCase == UseCase.RESULT_LIST || useCase == UseCase.ALPHA_INDEX) {
			return firstLanguage ? getKeys(getResultListLangA()) : getKeys(getResultListLangB());
		}
		throw new RuntimeException("Unsupported useCase: " + useCase + ". Please implement this!");
	}
	
	private ArrayList<String> getKeys(ArrayList<ValueFormat> formatters) {
		ArrayList<String> toReturn = new ArrayList<String>();
		for (ValueFormat vf : formatters) {
			toReturn.add(vf.getKey());
		}
		return toReturn;
	}
	public String toString(String term, List<LemmaVersion> meanings, boolean firstLanguage) {
		List<LemmaVersion> direct = getDirectMatches(term, meanings, firstLanguage);
		Set<LemmaVersion> complex = new HashSet<LemmaVersion>();
		complex.addAll(meanings);
		complex.removeAll(direct);
		StructuredEntry entry = new StructuredEntry();
		entry.setLemma("<b>" + term + "</b>");
		Map<String, List<String>> subEntries = new HashMap<String, List<String>>();
		List<ValueFormat> langAKeys = new ArrayList<ValueFormat>(firstLanguage ? getResultListLangA() : getResultListLangB());
		langAKeys.remove(0);
		for (LemmaVersion lemma : direct) {
			String key = format(lemma, langAKeys);
			List<String> variants = subEntries.get(key);
			if(variants == null) {
				variants = new ArrayList<String>();
				subEntries.put(key, variants);
			}
			variants.add(toString(lemma, UseCase.RESULT_LIST, !firstLanguage));
		}
		List<String> directMeanings = new ArrayList<String>();
		Set<Entry<String, List<String>>> entries = subEntries.entrySet();
		for (Entry<String, List<String>> entry2 : entries) {
			StringBuffer sb = new StringBuffer();
			sb.append(entry2.getKey() + " ");
			for(int i = 0; i < entry2.getValue().size(); i++) {
				sb.append("<i>");
				sb.append(entry2.getValue().get(i));
				sb.append("</i>");
				if(i < entry2.getValue().size()-1) {
					sb.append(", ");
				}
			}
			directMeanings.add(sb.toString());
		}
		// Complex Terms:
		subEntries.clear();
		langAKeys = new ArrayList<ValueFormat>(firstLanguage ? getResultListLangA() : getResultListLangB());
		for (LemmaVersion lemma : complex) {
			String key = format(lemma, langAKeys);
			// Regex: term must be surrounded by word boundaries >\\b<, upper/lower case doesn't matter >(?i)<
			String toReplace = "\\b(?i)" + term + "\\b";
			key = key.replaceAll(toReplace, " ~ ");
			List<String> variants = subEntries.get(key);
			if(variants == null) {
				variants = new ArrayList<String>();
				subEntries.put(key, variants);
			}
			variants.add(toString(lemma, UseCase.RESULT_LIST, !firstLanguage));
		}
		entries = subEntries.entrySet();
		for (Entry<String, List<String>> entry2 : entries) {
			StringBuffer sb = new StringBuffer();
			sb.append(entry2.getKey() + " ");
			for(int i = 0; i < entry2.getValue().size(); i++) {
				sb.append("<i>");
				sb.append(entry2.getValue().get(i));
				sb.append("</i>");
				if(i < entry2.getValue().size()-1) {
					sb.append(", ");
				}
			}
			directMeanings.add(sb.toString());
		}
		entry.setMeanings(directMeanings);
		return entry.toString();
	}
	private List<LemmaVersion> getDirectMatches(String term, List<LemmaVersion> meanings, boolean firstLanguage) {
		ArrayList<String> fields = getFields(UseCase.RESULT_LIST, firstLanguage);
		List<LemmaVersion> toReturn = new ArrayList<LemmaVersion>();
		for (LemmaVersion meaning : meanings) {
			String value = meaning.getEntryValue(fields.get(0));
			if(value.equals(term)) {
				toReturn.add(meaning);
			}
		}
		return toReturn;
	}
	
	public ArrayList<String> getSortListLangA() {
		return sortListLangA;
	}
	
	public ArrayList<String> getSortListLangB() {
		return sortListLangB;
	}
	public String getDictFieldLangA() {
		return dictFieldLangA;
	}
	public String getDictFieldLangB() {
		return dictFieldLangB;
	}

	public String getSortOrderLangA() {
		return sortOrderLangA;
	}
	public void setSortOrderLangA(String sortOrderLangA) {
		this.sortOrderLangA = sortOrderLangA;
	}
	public String getSortOrderLangB() {
		return sortOrderLangB;
	}
	public void setSortOrderLangB(String sortOrderLangB) {
		this.sortOrderLangB = sortOrderLangB;
	}
	
}

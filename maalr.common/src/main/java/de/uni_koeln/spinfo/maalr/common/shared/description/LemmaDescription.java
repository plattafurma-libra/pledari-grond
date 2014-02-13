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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.StructuredEntry;

/**
 * 
 * This class represents the basic dictionary structure, related to
 * <ul>
 * <li>The suggest- and modifiy-editor in frontend and backend</li>
 * <li>The representation and ordering of query results and</li>
 * </ul>
 * 
 * @author sschwieb
 *
 */
@XmlRootElement(name="lemmaDescription")
public class LemmaDescription implements Serializable {
	
	private static final long serialVersionUID = -666936366464661504L;
	
	@XmlElement(name="language")
	private List<Language> languages = new ArrayList<Language>();
	
	public Language getFirstLanguage() {
		return languages.get(0);
	}
	
	public Language getSecondLanguage() {
		return languages.get(1);
	}
	
	public Language getLanguage(boolean first) {
		if(first) {
			return languages.get(0);
		}
		return languages.get(1);
	}
	
	static class DBField implements Serializable {
		
		private static final long serialVersionUID = -2685941821834904988L;
		
		@XmlAttribute
		private String column;
		
		@XmlElementWrapper(name="allowed")
		@XmlElement(name="value")
		private List<String> allowedValues;
		
		@XmlAttribute(name="type")
		private ValueType type = ValueType.TEXT;
		
		@XmlAttribute(name="allowsNull")
		private boolean allowsNull = true;

		@Override
		public String toString() {
			return "DBField [column=" + column + ", allowedValues=" + allowedValues
					+ ", type=" + type + ", allowsNull=" + allowsNull + "]";
		}

	}
	
	static class FormattedIndexField implements Serializable {
		
		private static final long serialVersionUID = -3914110096820231128L;
		
		@XmlAttribute
		private String column;
		
		@XmlAttribute
		private String format;

		@Override
		public String toString() {
			return "FormattedIndexField [column=" + column + ", format=" + format
					+ "]";
		}
	}
	
	static class Results implements Serializable {
		
		private static final long serialVersionUID = 4423016975803255789L;

		@XmlElementWrapper(name="fields")
		@XmlElement(name="field")
		private List<FormattedIndexField> fields;

		@Override
		public String toString() {
			return "Results [fields=" + fields + "]";
		}

		public ArrayList<ValueFormat> getFormats() {
			ArrayList<ValueFormat> formats = new ArrayList<ValueFormat>();
			for (FormattedIndexField field : fields) {
				ValueFormat format = new ValueFormat();
				format.setFormat(field.format);
				format.setKey(field.column);
				formats.add(format);
			}
			return formats;
		}

	}
	
	static class Editors implements Serializable {
		
		private static final long serialVersionUID = 3691320151505496726L;

		@XmlElement(name="frontend_editor")
		private Editor frontendEditor;
		
		@XmlElement(name="backend_editor")
		private Editor backendEditor;

		@Override
		public String toString() {
			return "Editors [frontendEditor=" + frontendEditor
					+ ", backendEditor=" + backendEditor + "]";
		}

	}
	
	static class Editor implements Serializable {
		
		private static final long serialVersionUID = 298032125361937577L;
		@XmlElementWrapper(name="fields")
		@XmlElement(name="field")
		private List<DBField> fields;

		@Override
		public String toString() {
			return "Editor [fields=" + fields + "]";
		}

		public ArrayList<String> getFieldIds() {
			ArrayList<String> ids = new ArrayList<String>();
			for (DBField field : fields) {
				ids.add(field.column);
			}
			return ids;
		}
		
	}
	
	public static class Language implements Serializable {
		
		private static final long serialVersionUID = -6107461479348341257L;
		
		@XmlAttribute
		private String id;
		
		public String getId() {
			return id;
		}

		@XmlAttribute
		private String mainColumn;
		
		public String getMainColumn() {
			return mainColumn;
		}

		@XmlElement
		private Editors editors;
		
		@XmlElement
		private Results results;
		
		@Override
		public String toString() {
			return "Language [id=" + id + ", mainColumn=" + mainColumn
					+ ", editors=" + editors + ", results=" + results
					+ "]";
		}
		
		

	}
	
	
	@Override
	public String toString() {
		return "NewLemmaDescription [languages=" + languages + "]";
	}

	public ArrayList<ValueFormat> getResultList(boolean firstLanguage) {
		return getLanguage(firstLanguage).results.getFormats();
	}
	
	public String getLanguageName(boolean firstLanguage) {
		return getLanguage(firstLanguage).id;
	}
	
	public String toString(LemmaVersion lemma, UseCase useCase, boolean firstLanguage) {
		return toString(lemma, useCase, firstLanguage, null);
	}
	
	public String toString(LemmaVersion lemma, UseCase useCase, boolean firstLanguage, Escaper escaper) {
		switch(useCase) {
			case RESULT_LIST : {
				List<ValueFormat> keys = getResultList(firstLanguage);
				return format(lemma, keys, escaper);
			}
			case ALPHA_INDEX: {
				List<ValueFormat> keys = getResultList(firstLanguage);
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
			return getLanguage(firstLanguage).editors.frontendEditor.getFieldIds();
		}
		if(useCase == UseCase.FIELDS_FOR_ADVANCED_EDITOR) {
			return getLanguage(firstLanguage).editors.backendEditor.getFieldIds();
		}
		if(useCase == UseCase.RESULT_LIST || useCase == UseCase.ALPHA_INDEX) {
			return getKeys(getResultList(firstLanguage));
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
		List<ValueFormat> langAKeys = new ArrayList<ValueFormat>(getResultList(firstLanguage));
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
		langAKeys = new ArrayList<ValueFormat>(getResultList(firstLanguage));
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
	
	public String[] getSortList(boolean firstLanguage) {
		Language language = getLanguage(firstLanguage);
		return new String[] {language.getMainColumn(), language.getMainColumn()+"_sort"};
	}
	

	public ArrayList<ValueSpecification> getValues(UseCase useCase) {
		ArrayList<ValueSpecification> values = new ArrayList<ValueSpecification>();
		if(useCase == UseCase.FIELDS_FOR_ADVANCED_EDITOR) {
			values.addAll(getValueSpecifications(languages.get(0).editors.backendEditor.fields));
			values.addAll(getValueSpecifications(languages.get(1).editors.backendEditor.fields));
		} else {
			values.addAll(getValueSpecifications(languages.get(0).editors.frontendEditor.fields));
			values.addAll(getValueSpecifications(languages.get(1).editors.frontendEditor.fields));
		}
		return values;
	}
	
	private ArrayList<ValueSpecification> getValueSpecifications(List<DBField> fields) {
		ArrayList<ValueSpecification> list = new ArrayList<ValueSpecification>();
		for (DBField field : fields) {
			ValueSpecification spec = new ValueSpecification(field.column, field.type);
			if(field.type == ValueType.ENUM) {
				ValueValidator validator = new ChoiceValidator(field.allowsNull, field.allowedValues);
				spec.setValidator(validator);
			} else if (!field.allowsNull) {
				spec.setValidator(new NotNullValidator());
			}
			list.add(spec);
		}
		return list;
	}

	public String getDictField(boolean firstLanguage) {
		return getLanguage(firstLanguage).mainColumn;
	}

	public String getSortOrder(boolean firstLanguage) {
		return getLanguage(firstLanguage).mainColumn + "_sort";
	}
	
	public ArrayList<String> getEditorFields(boolean firstLanguage) {
		return getLanguage(firstLanguage).editors.backendEditor.getFieldIds();
	}

}

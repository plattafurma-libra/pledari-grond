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
import java.util.logging.Logger;

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
 * <li>The alphabetical index of the dictionary</li>
 * </ul>
 * It is configured through the lemma-description.xml configuration file.
 * 
 * @author sschwieb
 *
 */
@XmlRootElement(name="lemmaDescription")
public class LemmaDescription implements Serializable {
	
	private static final long serialVersionUID = -666936366464661504L;
	
	@XmlElement(name="language")
	private List<Language> languages = new ArrayList<Language>();
	
	static class DBField implements Serializable {
		
		private static final long serialVersionUID = -2685941821834904988L;
		
		@XmlAttribute
		private String dbId;
		
		@XmlElementWrapper(name="allowed")
		@XmlElement(name="value")
		private List<String> allowedValues;
		
		@XmlAttribute(name="type")
		private ValueType type = ValueType.TEXT;
		
		@XmlAttribute(name="allowsNull")
		private boolean allowsNull = true;

		@Override
		public String toString() {
			return "DBField [dbId=" + dbId + ", allowedValues=" + allowedValues
					+ ", type=" + type + ", allowsNull=" + allowsNull + "]";
		}

	}
	
	static class Dictionary implements Serializable {
		
		private static final long serialVersionUID = 8540014692798449169L;
		
		@XmlElement(name="field")
		private IndexField field;

		@Override
		public String toString() {
			return "Dictionary [field=" + field + "]";
		}
		
	}
	
	static class IndexField implements Serializable {
		
		private static final long serialVersionUID = -3914110096820231128L;
		
		@XmlAttribute
		private String idxId;

		@Override
		public String toString() {
			return "IndexField [idxId=" + idxId + "]";
		}
		
	}
	
	static class FormattedIndexField implements Serializable {
		
		private static final long serialVersionUID = -3914110096820231128L;
		
		@XmlAttribute
		private String idxId;
		
		@XmlAttribute
		private String format;

		@Override
		public String toString() {
			return "FormattedIndexField [idxId=" + idxId + ", format=" + format
					+ "]";
		}

		
	}
	
	static class Results implements Serializable {
		
		private static final long serialVersionUID = 4423016975803255789L;

		@XmlElementWrapper(name="fields")
		@XmlElement(name="field")
		private List<FormattedIndexField> fields;

		@XmlElement(name="sort_order")
		private SortOrder sortOrder;

		@Override
		public String toString() {
			return "Results [fields=" + fields + ", sortOrder=" + sortOrder
					+ "]";
		}

		public ArrayList<ValueFormat> getFormats() {
			ArrayList<ValueFormat> formats = new ArrayList<ValueFormat>();
			for (FormattedIndexField field : fields) {
				ValueFormat format = new ValueFormat();
				format.setFormat(field.format);
				format.setKey(field.idxId);
				formats.add(format);
			}
			return formats;
		}

	}
	
	static class SortOrder implements Serializable {
		
		private static final long serialVersionUID = 8571167186246264418L;
		@XmlElementWrapper(name="fields")
		@XmlElement(name="field")
		private List<IndexField> fields;

		@Override
		public String toString() {
			return "SortOrder [fields=" + fields + "]";
		}

		public ArrayList<String> getValues() {
			ArrayList<String> values = new ArrayList<String>();
			for (IndexField field : fields) {
				values.add(field.idxId);
			}
			return values;
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
				ids.add(field.dbId);
			}
			return ids;
		}
		
	}
	
	
	static class Language implements Serializable {
		
		private static final long serialVersionUID = -6107461479348341257L;

		@XmlAttribute
		private String id;
		
		@XmlElement
		private Editors editors;
		
		@XmlElement
		private Results results;
		
		@XmlElement
		private Dictionary dictionary;

		@Override
		public String toString() {
			return "Language [id=" + id + ", editors=" + editors + ", results="
					+ results + ", dictionary=" + dictionary + "]";
		}

	}
	
	
	@Override
	public String toString() {
		return "NewLemmaDescription [languages=" + languages + "]";
	}

	public ArrayList<ValueFormat> getResultListLangA() {
		return languages.get(0).results.getFormats();
	}

	public ArrayList<ValueFormat> getResultListLangB() {
		return languages.get(1).results.getFormats();
	}

	public ArrayList<String> getEditorLangA() {
		return languages.get(0).editors.backendEditor.getFieldIds();
	}
	
	public ArrayList<String> getEditorLangB() {
		return languages.get(1).editors.backendEditor.getFieldIds();
	}
	
	public String getLanguageName(boolean firstLanguage) {
		if(firstLanguage) return languages.get(0).id;
		return languages.get(1).id;
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
				return languages.get(0).editors.frontendEditor.getFieldIds();
			} else {
				return languages.get(1).editors.frontendEditor.getFieldIds();
			}
		}
		if(useCase == UseCase.FIELDS_FOR_ADVANCED_EDITOR) {
			if(firstLanguage) {
				return languages.get(0).editors.backendEditor.getFieldIds();
			} else {
				return languages.get(1).editors.backendEditor.getFieldIds();
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
		return languages.get(0).results.sortOrder.getValues();
	}

	public ArrayList<String> getSortListLangB() {
		return languages.get(1).results.sortOrder.getValues();
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
			ValueSpecification spec = new ValueSpecification(field.dbId, field.type);
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

	public String getDictFieldLangA() {
		return languages.get(0).dictionary.field.idxId;
	}
	public String getDictFieldLangB() {
		return languages.get(1).dictionary.field.idxId;
	}

	public String getSortOrderLangA() {
		List<IndexField> fields = languages.get(0).results.sortOrder.fields;
		return fields.get(fields.size()-1).idxId;
	}
	public String getSortOrderLangB() {
		List<IndexField> fields = languages.get(1).results.sortOrder.fields;
		return fields.get(fields.size()-1).idxId;
	}
	

}

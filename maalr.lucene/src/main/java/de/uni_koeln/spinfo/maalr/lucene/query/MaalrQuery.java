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
package de.uni_koeln.spinfo.maalr.lucene.query;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class MaalrQuery implements Serializable {

	@Override
	public String toString() {
		return "MaalrQuery [values=" + values + "]";
	}

	private static final long serialVersionUID = 7866278181499489555L;
	
	private TreeMap<String, String> values = new TreeMap<String, String>();
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaalrQuery other = (MaalrQuery) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	public boolean isSuggestions() {
		String value = values.get("suggestions");
		if(value == null) return false;
		return Boolean.parseBoolean(value);
	}

	public void setSuggestions(boolean suggestions) {
		values.put("suggestions", suggestions+"");
	}

	public MaalrQuery() {
	}

	public void setQueryValue(String key, String value) {
		if(key == null) return;
		if(value == null) {
			values.remove(key);
		} else {
			values.put(key, value);
		}
	}
	
	public Map<String, String> getQueryMap() {
		return values;
	}

	public static MaalrQuery parse(String historyToken) {
		String[] parts = historyToken.split("&");
		MaalrQuery query = new MaalrQuery();
		for (String part : parts) {
			String[] pair = part.split("=");
			if(pair.length == 2) {
				String key = pair[0];
				String value = pair[1];
				query.setQueryValue(key, value);
			}
		}
		return query;
	}
	
	private static TreeMap<String, String> removeNullValues(Map<String,String> input) {
		//Logger.getLogger(MaalrQuery.class.getName()).info("Input: " + input);
		TreeMap<String, String> copy = new TreeMap<String, String>();
		Set<Entry<String, String>> entries = input.entrySet();
		for (Entry<String, String> entry : entries) {
			if(entry.getValue() != null) {
				copy.put(entry.getKey(), entry.getValue());
			}
		}
		//Logger.getLogger(MaalrQuery.class.getName()).info("Output: " + copy);
		return copy;
	}

	public static String toUrl(TreeMap<String, String> map) {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, String>> entries = removeNullValues(map).entrySet().iterator();
		while(entries.hasNext()) {
			Entry<String, String> entry = entries.next();
			sb.append(entry.getKey()+"="+entry.getValue());
			if(entries.hasNext()) {
				sb.append("&");
			}
		}
		String url = sb.toString();
		return url;
	}

	public String toURL() {
		return toUrl(values);
	}

	public int getPageSize() {
		String size = values.get("pageSize");
		if(size == null) return 15;
		try {
			return Integer.parseInt(size);
		} catch (NumberFormatException e) {
			return 15;
		}
	}

	public boolean isHighlight() {
		String value = values.get("highlight");
		if(value == null) return false;
		return Boolean.parseBoolean(value);
	}
	
	public void setHighlight(boolean highlight) {
		values.put("highlight", highlight+"");
	}

	public int getPageNr() {
		String value = values.get("pageNr");
		if(value == null) return 0;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public TreeMap<String, String> getValues() {
		return values;
	}

	public void setValues(TreeMap<String, String> values) {
		this.values = values;
	}

	public void setPageNr(int i) {
		values.put("pageNr", i+"");
	}

	public void setPageSize(int i) {
		values.put("pageSize", i+"");
	}
	
	public String getValue(String key) {
		return values.get(key);
	}

}

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
package de.uni_koeln.spinfo.maalr.conjugator.parser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MSU implements DBObject, Serializable {

	private static final long serialVersionUID = 1L;

	private ObjectId _id;

	private String fullform;
	private String infinitiv;
	private String root;

	private String tempus;
	private String pronoun;
	private String person;
	private String numerus;
	private String genus;

	public static final String preschent = "preschent";
	public static final String imperfect = "imperfect";
	public static final String conjunctiv = "conjunctiv";
	public static final String cundizional = "cundizional";
	public static final String particip_perfect = "particip perfect";

	public static final String firstPerson = "1";
	public static final String secondPerson = "2";
	public static final String thirdPerson = "3";

	public static final String singular = "singular";
	public static final String plural = "plural";

	public static final String masculine = "masculine";
	public static final String feminine = "feminine";
	public static final String masculine_feminine = "masculine/feminine";

	public static final String empty = "empty";
	public static final String jau = "jau";
	public static final String ti = "ti";
	public static final String el_ella = "el/ella";
	public static final String nus = "nus";
	public static final String vus = "vus";
	public static final String els_ellas = "els/ellas";

	public MSU(String fullform, String infinitiv, String root, String tempus,
			String pronoun, String person, String numerus, String genus) {

		this.fullform = fullform;
		this.infinitiv = infinitiv;
		this.root = root;

		this.tempus = tempus;
		this.pronoun = pronoun;
		this.person = person;
		this.numerus = numerus;
		this.genus = genus;

	}

	public DBObject bsonFromPojo() {
		BasicDBObject document = new BasicDBObject();

		document.put("_id", this._id);
		document.put("fullform", this.fullform);
		document.put("infinitiv", this.infinitiv);
		document.put("root", this.root);

		document.put("tempus", this.tempus);
		document.put("pronoun", this.pronoun);
		document.put("person", this.person);
		document.put("genus", this.genus);
		document.put("numerus", this.numerus);

		return document;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map toMap() {

		Map<String, Object> map = new HashMap<String, Object>();

		if (this._id != null)
			map.put("_id", this._id);
		if (this.fullform != null)
			map.put("fullform", this.fullform);
		if (this.infinitiv != null)
			map.put("infinitiv", this.infinitiv);

		//
		if (this.root != null)
			map.put("root", this.root);

		if (this.tempus != null)
			map.put("tempus", this.tempus);
		if (this.pronoun != null)
			map.put("pronoun", this.pronoun);
		if (this.person != null)
			map.put("person", this.person);
		if (this.genus != null)
			map.put("genus", this.genus);
		if (this.numerus != null)
			map.put("numerus", this.numerus);

		return map;

	}

	@Override
	public Object put(String field, Object object) {

		if (field.equals("_id")) {
			this._id = (ObjectId) object;
			return object;
		}
		if (field.equals("fullform")) {
			this.fullform = (String) object;
			return object;
		}
		if (field.equals("infinitiv")) {
			this.infinitiv = (String) object;
			return object;
		}

		if (field.equals("root")) {
			this.root = (String) object;
			return object;
		}

		if (field.equals("tempus")) {
			this.tempus = (String) object;
			return object;
		}
		if (field.equals("pronoun")) {
			this.pronoun = (String) object;
			return object;
		}
		if (field.equals("person")) {
			this.person = (String) object;
			return object;
		}
		if (field.equals("genus")) {
			this.genus = (String) object;
			return object;
		}
		if (field.equals("numerus")) {
			this.numerus = (String) object;
			return object;
		}

		return null;
	}

	@Override
	public void putAll(BSONObject o) {
		for (String key : o.keySet())
			put(key, o.get(key));

	}

	@SuppressWarnings("unchecked")
	@Override
	public void putAll(@SuppressWarnings("rawtypes") Map m) {
		for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) m
				.entrySet())
			put(entry.getKey().toString(), entry.getValue());

	}

	@Override
	public Object get(String key) {

		if (key.equals("_id"))
			return this._id;
		if (key.equals("fullform"))
			return this.fullform;
		if (key.equals("infinitiv"))
			return this.infinitiv;

		if (key.equals("root"))
			return this.root;

		if (key.equals("tempus"))
			return this.tempus;
		if (key.equals("pronoun"))
			return this.pronoun;
		if (key.equals("person"))
			return this.person;
		if (key.equals("genus"))
			return this.genus;
		if (key.equals("numerus"))
			return this.numerus;

		return null;
	}

	@Override
	public boolean containsField(String s) {

		return (s.equals("_id") || s.equals("fullform")
				|| s.equals("infinitiv") || s.equals("root") ||

				s.equals("") || s.equals("tempus") || s.equals("pronoun")
				|| s.equals("person") || s.equals("genus") || s
					.equals("numerus"));

	}

	@Override
	public Set<String> keySet() {
		Set<String> set = new HashSet<String>();

		set.add("_id");
		set.add("fullform");
		set.add("infinitiv");

		set.add("root");

		set.add("");
		set.add("tempus");
		set.add("pronoun");
		set.add("person");
		set.add("genus");
		set.add("numerus");

		return set;
	}

	@Override
	public Object removeField(String key) {
		throw new RuntimeException("Unsupported method.");
	}

	@Override
	public boolean containsKey(String s) {
		return containsField(s);
	}

	@Override
	public void markAsPartialObject() {
		throw new RuntimeException("Method not implemented.");
	}

	@Override
	public boolean isPartialObject() {
		return false;
	}

	public String getTempus() {
		return tempus;
	}

	public void setTempus(String tempus) {
		this.tempus = tempus;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getNumerus() {
		return numerus;
	}

	public void setNumerus(String numerus) {
		this.numerus = numerus;
	}

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getPronoun() {
		return pronoun;
	}

	public void setPronoun(String pronoun) {
		this.pronoun = pronoun;
	}

	public String getFullform() {
		return fullform;
	}

	public void setFullform(String fullform) {
		this.fullform = fullform;
	}

	public ObjectId getId() {
		return this._id;
	}

	public void setId(ObjectId _id) {
		this._id = _id;
	}

	public void generateId() {
		if (this._id == null)
			this._id = new ObjectId();
	}

	public String getInfinitiv() {
		return infinitiv;
	}

	public void setInfinitiv(String infinitiv) {
		this.infinitiv = infinitiv;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

}

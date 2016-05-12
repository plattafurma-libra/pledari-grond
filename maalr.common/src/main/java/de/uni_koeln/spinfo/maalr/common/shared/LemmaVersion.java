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
package de.uni_koeln.spinfo.maalr.common.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class LemmaVersion implements Serializable {

	private static final long serialVersionUID = -3937300849636141916L;

	public enum Status {
		DELETED("Deleted"), NEW_ENTRY("New"), NEW_MODIFICATION("Modified"), UNDEFINED("Undefined");
		
		private String value;

		private Status(String value) {
			this.value = value;
		}

		public String getStateName() {
			return value;
		}
	}

	public enum Verification {
		REJECTED ("Rejected"), ACCEPTED("Accepted"), OUTDATED ("Outdated"), UNVERIFIED ("Unverified");
		
		private String value;

		private Verification(String value) {
			this.value = value;
		}

		public String getVerificationName() {
			return value;
		}
	}
	
	public static final String TIMESTAMP = "maalr_timestamp";

	public static final String CREATOR = "maalr_creator";

	public static final String ID = "internal_id";
	
	public static final String LEXENTRY_ID = "entry_id";

	public static final String STATUS = "maalr_status";
	
	public static final String VERIFICATION = "maalr_verification";
	
	public static final String VERIFIER = "maalr_verifier";

	private static final String VOTER = "maalr_voter";
	
	public static final Set<String> MAALR_KEYS;

	public static final String COMMENT = "maalr_comment";

	public static final String IP_ADDRESS = "maalr_ip";

	public static final String CREATOR_ROLE = "maalr_creator_role";

	public static final HashSet<String> PUBLIC_MAALR_KEYS;

	public static final String EMAIL = "maalr_email";

	public static final String OVERLAY_LANG2 = "maalr_overlay_lang2";
	public static final String OVERLAY_LANG1 = "maalr_overlay_lang1";
	
	static {
		MAALR_KEYS = new HashSet<String>();
		MAALR_KEYS.add(TIMESTAMP);
		MAALR_KEYS.add(CREATOR);
		MAALR_KEYS.add(ID);
		MAALR_KEYS.add(STATUS);
		MAALR_KEYS.add(VOTER);
		MAALR_KEYS.add(IP_ADDRESS);
		MAALR_KEYS.add(CREATOR_ROLE);
		MAALR_KEYS.add(VERIFICATION);
		MAALR_KEYS.add(VERIFIER);
		MAALR_KEYS.add(LEXENTRY_ID);
		PUBLIC_MAALR_KEYS = new HashSet<String>();
		PUBLIC_MAALR_KEYS.add(ID);
		PUBLIC_MAALR_KEYS.add(STATUS);
		PUBLIC_MAALR_KEYS.add(VERIFICATION);
		PUBLIC_MAALR_KEYS.add(LEXENTRY_ID);
	}

	private LightUserInfo userInfo;
	
	private HashMap<String, String> entryValues = new HashMap<String, String>();
	private HashMap<String, String> maalrValues = new HashMap<String, String>();

	@XmlTransient
	private List<CorpusResponse> quotations;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((entryValues == null) ? 0 : entryValues.hashCode());
		result = prime * result
				+ ((maalrValues == null) ? 0 : maalrValues.hashCode());
		result = prime * result
				+ ((userInfo == null) ? 0 : userInfo.hashCode());
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
		LemmaVersion other = (LemmaVersion) obj;
		if (entryValues == null) {
			if (other.entryValues != null)
				return false;
		} else if (!entryValues.equals(other.entryValues))
			return false;
		if (maalrValues == null) {
			if (other.maalrValues != null)
				return false;
		} else if (!maalrValues.equals(other.maalrValues))
			return false;
		if (userInfo == null) {
			if (other.userInfo != null)
				return false;
		} else if (!userInfo.equals(other.userInfo))
			return false;
		return true;
	}

	public LemmaVersion() {
		setStatus(Status.UNDEFINED);
		setVerification(Verification.UNVERIFIED);
		setTimestamp(0L);
	}

	@XmlAttribute
	public Status getStatus() {
		if(maalrValues.get(STATUS) == null) return null;
		return Status.valueOf((String) maalrValues.get(STATUS));
	}

	public void setStatus(Status status) {
		maalrValues.put(STATUS, status.name());
	}
	
	@XmlAttribute
	public Verification getVerification() {
		if(maalrValues.get(VERIFICATION) == null) return null;
		return Verification.valueOf((String) maalrValues.get(VERIFICATION));
	}

	public void setVerification(Verification status) {
		maalrValues.put(VERIFICATION, status.name());
	}
	
	@XmlElement(name="creator")
	public LightUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(LightUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	@XmlElementWrapper(name="fields")
	public HashMap<String, String> getEntryValues() {
		return entryValues;
	}
	
	@XmlTransient
	public HashMap<String, String> getMaalrValues() {
		return maalrValues;
	}

	public void setEntryValues(HashMap<String, String> entryValues) {
		this.entryValues = entryValues;
	}

	@XmlAttribute(name="timeStamp")
	public Long getTimestamp() {
		if(maalrValues.get(TIMESTAMP) == null) return 0L;
		return Long.parseLong(maalrValues.get(TIMESTAMP));
	}

	public void setTimestamp(Long timestamp) {
		maalrValues.put(TIMESTAMP, timestamp+"");
	}

	@XmlElement(name="userId")
	public String getUserId() {
		return (String) maalrValues.get(CREATOR);
	}

	public void setUserId(String userId) {
		maalrValues.put(CREATOR, userId);
	}

	public void setVerifierId(String verifierId) {
		maalrValues.put(VERIFIER, verifierId);
	}
	
	@XmlElement(name="verifierId")
	public String getVerifierId() {
		return maalrValues.get(VERIFIER);
	}

	@XmlAttribute(name="id")
	public Integer getInternalId() {
		String idString = maalrValues.get(ID);
		if(idString == null) return null;
		Integer id = Integer.parseInt(idString);
		return id;
	}

	public void setInternalId(Integer id) {
		maalrValues.put(ID, id+"");
	}

	public boolean isApproved() {
		String verification = (String) maalrValues.get(VERIFICATION);
		if(verification == null) return false;
		return Verification.ACCEPTED == Verification.valueOf(verification);
	}

	@XmlAttribute
	public String getLexEntryId() {
		return maalrValues.get(LEXENTRY_ID);
	}

	public void setIP(String ip) {
		maalrValues.put(IP_ADDRESS, ip);
	}
	
	public void setCreatorRole(Role role) {
		if(role == null) {
			maalrValues.put(CREATOR_ROLE, null);
		} else {
			maalrValues.put(CREATOR_ROLE, role.toString());
		}
	}
	
	@XmlAttribute(name="creatorRole")
	public Role getCreatorRole() {
		if(maalrValues.get(CREATOR_ROLE) == null) return null; 
		try {
			return Role.valueOf(maalrValues.get(CREATOR_ROLE));
		} catch (Exception e) {
			return null;
		}
	}
	
	@XmlAttribute(name="ip")
	public String getIP() {
		return maalrValues.get(IP_ADDRESS);
	}

	public boolean equalData(LemmaVersion other) {
		Map<String, String> myValues = getEntryValues();
		Map<String, String> otherValues = other.getEntryValues();
		Set<String> all = new HashSet<String>();
		all.addAll(myValues.keySet());
		all.addAll(otherValues.keySet());
		for (String key : all) {
			if(myValues.get(key) == null && otherValues.get(key) != null) {
				return false;
			}
			if(myValues.get(key) != null && otherValues.get(key) == null) {
				return false;
			}
			if(myValues.get(key) == null) continue;
			if(!myValues.get(key).equals(otherValues.get(key))) {
				return false;
			}
		}
		return true;
	}

	public String getEntryValue(String key) {
		return entryValues.get(key);
	}

	public void putEntryValue(String key, String value) {
		entryValues.put(key, value);
	}

	public void removeEntryValue(String key) {
		entryValues.remove(key);
	}

	public void putMaalrValue(String key, String value) {
		maalrValues.put(key, value);
	}

	@Override
	public String toString() {
		return "LemmaVersion [entryValues=" + entryValues + ", maalrValues="
				+ maalrValues + "]";
	}

	public void setMaalrValues(HashMap<String, String> map) {
		this.maalrValues = map;
	}

	public void setValue(String key, String value) {
		entryValues.put(key, value);
	}
	
	public void addQuotation(List<CorpusResponse> quotations) {
		this.quotations = quotations;
	}

}

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LexEntry implements Serializable {

	private static final long serialVersionUID = 2842502932915291640L;
	public static final String ID = "_id";
	public static final String VERSIONS = "versions";
	public static final String CURRENT = "current";
	public static final String INTERNAL_ID = "internal_id";
	public static final String CHANGE_STAMP = "change_stamp";

	@XmlElement(name="version")
	@XmlElementWrapper(name="versions")
	private ArrayList<LemmaVersion> versionHistory;

	@XmlAttribute(name="nextId")
	private int nextId = 0;
	
	@XmlAttribute(name="currentVersion")
	private Integer currentId;
	
	@XmlAttribute
	private String id;
	
	@XmlAttribute(name="changeStamp")
	private String changeStamp;
	
	
	public String getChangeStamp() {
		return changeStamp;
	}

	public void setChangeStamp(String changeStamp) {
		this.changeStamp = changeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentId == null) ? 0 : currentId.hashCode());
		result = prime * result
				+ ((versionHistory == null) ? 0 : versionHistory.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + nextId;
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
		LexEntry other = (LexEntry) obj;
		if (currentId == null) {
			if (other.currentId != null)
				return false;
		} else if (!currentId.equals(other.currentId))
			return false;
		if (versionHistory == null) {
			if (other.versionHistory != null)
				return false;
		} else if (!versionHistory.equals(other.versionHistory))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nextId != other.nextId)
			return false;
		return true;
	}

	
	
	@Override
	public String toString() {
		return "LexEntry [versions=" + versionHistory + ", id=" + id
				+ "]";
	}

	public LexEntry() {
		
	}
	
	public LexEntry(final LemmaVersion entry) {
		addLemma(entry);
		setCurrent(entry);
	}

	public ArrayList<LemmaVersion> getVersionHistory() {
		loadVersions();
		return versionHistory;
	}

	public ArrayList<LemmaVersion> getUnapprovedVersions() {
		ArrayList<LemmaVersion> versions = getVersionHistory();
		ArrayList<LemmaVersion> unapproved = new ArrayList<LemmaVersion>();
		for (LemmaVersion lv : versions) {
			if(lv.getVerification() == Verification.UNVERIFIED) {
				unapproved.add(lv);
			}
		}
		return unapproved;
	}

	public boolean hasUnapprovedVersions() {
		return getUnapprovedVersions().size() > 0;
	}

	private synchronized void loadVersions() {
		if (versionHistory != null)
			return;
		if(versionHistory == null) {
			versionHistory = new ArrayList<LemmaVersion>();
		}
		if(versionHistory.size() > 1) {
			sort();
		}
	}

	private void sort() {
		Collections.sort(versionHistory, new Comparator<LemmaVersion>() {

			@Override
			public int compare(LemmaVersion o1, LemmaVersion o2) {
				long value = o2.getTimestamp() - o1.getTimestamp();
				if( value < 0 ) return -1;
				if( value > 0 ) return 1;
				return 0;
			}
			
		});
	}

	public boolean addLemma(final LemmaVersion entry) {
		loadVersions();
		entry.setInternalId(nextId++);
		versionHistory.add(0,entry);
		return true;
	}

	public LemmaVersion getCurrent() {
		List<LemmaVersion> versions = getVersionHistory();
		// FIXME: Implement more efficient way to get current version.
		for (LemmaVersion lv : versions) {
			if (lv.getInternalId().equals(currentId)) {
				return lv;
			}
		}
		return null;
	}

	public void setCurrent(final LemmaVersion entry) {
		currentId = entry.getInternalId();
	}

	public Status getStatus() {
		LemmaVersion current = getCurrent();
		return current.getStatus();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void setLemmaVersions(ArrayList<LemmaVersion> list) {
		this.versionHistory = list;
	}

	public LemmaVersion getMostRecent() {
		List<LemmaVersion> unapproved = getUnapprovedVersions();
		LemmaVersion current = getCurrent();
		for (LemmaVersion lv : unapproved) {
			if(lv.getTimestamp() > current.getTimestamp()) {
				return lv;
			}
		}
		return current;
	}

	public void setNextInternalId(int nextId) {
		this.nextId = nextId;
	}

	public int getNextInternalId() {
		return nextId;
	}
	
	public void setCurrentId(int id) {
		this.currentId = id;
	}
	
	public int getCurrentId() {
		return currentId;
	}

}
